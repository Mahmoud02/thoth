# Technical Notes - Thoth Project

## Database Persistence Issues and Solutions

### Problem: Spring Data JDBC @Modifying Queries Setting Functions Column to NULL

#### Issue Description
When using Spring Data JDBC `@Modifying` queries to update JSONB columns in PostgreSQL, the functions column was being set to `null` instead of the expected JSON data, even though the query executed successfully and returned 1 affected row.

#### Root Cause Analysis
1. **Spring Data JDBC @Modifying Query Limitations**: The `@Modifying` annotation in Spring Data JDBC has issues with complex parameter types like `PGobject`
2. **Parameter Binding Issues**: Spring Data JDBC doesn't properly handle `PGobject` parameters in `@Query` annotations

#### Symptoms
- Query executes successfully (returns 1 affected row)
- Functions column becomes `null` in database
- No exceptions thrown during execution
- Debug logs show correct data before repository call

#### Solutions Tested

##### Solution 1: String Parameter with ::jsonb Casting
```java
@Modifying
@Query(value = "UPDATE buckets SET functions = :functions::jsonb WHERE id = :bucketId")
int updateFunctionsConfig(@Param("bucketId") Long bucketId, @Param("functions") String functions);
```
**Result**: ✅ Works - Converts Map to JSON string and lets PostgreSQL handle JSONB conversion

##### Solution 2: JdbcTemplate with PGobject (Recommended)
```java
public int updateFunctionsConfig(Long bucketId, PGobject functions) {
    String sql = "UPDATE buckets SET functions = ? WHERE id = ?";
    int updated = jdbcTemplate.update(sql, functions, bucketId);
    return updated;
}
```
**Result**: ✅ Works - Direct control over parameter binding and PostgreSQL driver handles PGobject properly

#### Recommended Approach: JdbcTemplate with PGobject

**Why JdbcTemplate is Preferred:**
1. **Direct Control**: Full control over SQL execution and parameter binding
2. **PGobject Support**: PostgreSQL driver properly handles PGobject conversion to JSONB
3. **No Spring Data JDBC Limitations**: Bypasses Spring Data JDBC's parameter binding issues
4. **Better Debugging**: Easier to debug and trace execution
5. **Consistent Behavior**: More predictable than Spring Data JDBC abstractions

**Implementation:**
```java
@Override
public void updateFunctionsConfig(Long bucketId, Map<String, Object> functionsConfigMap) {
    var jsonbValue = jsonbWritingConverter.convert(functionsConfigMap);
    updateFunctionsConfig(bucketId, jsonbValue);
}

public int updateFunctionsConfig(Long bucketId, PGobject functions) {
    String sql = "UPDATE buckets SET functions = ? WHERE id = ?";
    int updated = jdbcTemplate.update(sql, functions, bucketId);
    return updated;
}
```

#### Key Learnings

1. **Spring Data JDBC Limitations**: `@Modifying` queries have limitations with complex parameter types
2. **PGobject vs String**: PGobject works better with JdbcTemplate, String works better with `@Modifying` queries
3. **Transaction Management**: Always ensure proper `@Transactional` annotations for data modifications
4. **Debugging Strategy**: Always verify database state after updates, not just query execution success

#### Best Practices

1. **Use JdbcTemplate for Complex Operations**: When dealing with JSONB, custom types, or complex SQL
2. **Use Spring Data JDBC for Simple CRUD**: For standard entity operations
3. **Always Verify Updates**: Check database state after modifications
4. **Proper Error Handling**: Include fallback mechanisms for critical operations
5. **Comprehensive Logging**: Log parameter values and results for debugging

#### Related Files
- `src/main/java/com/mahmoud/thoth/infrastructure/store/impl/sqlite/SQLiteBucketStore.java`
- `src/main/java/com/mahmoud/thoth/infrastructure/store/impl/sqlite/repository/BucketRepository.java`
- `src/main/java/com/mahmoud/thoth/infrastructure/store/impl/sqlite/converter/JsonbWritingConverter.java`

## Dynamic Function Discovery System

### Overview
The system now dynamically discovers and provides metadata for all available bucket functions using Spring's dependency injection and custom annotations.

### Implementation Details

#### 1. FunctionMetadata Annotation
```java
@FunctionMetadata(
    name = "File Size Limit",
    description = "Restricts the maximum file size that can be uploaded to the bucket",
    properties = {"maxSizeBytes", "order"},
    propertyTypes = {"Long", "Integer"},
    propertyRequired = {true, true},
    propertyDescriptions = {
        "Maximum file size in bytes",
        "Execution order (lower numbers execute first)"
    },
    propertyDefaults = {"10485760", "1"}
)
```

#### 2. Dynamic Discovery Process
1. **Spring Auto-Discovery**: All `@Component` classes implementing `BucketFunction` are automatically injected
2. **Annotation Processing**: `FunctionInfoService` reads `@FunctionMetadata` annotations at runtime
3. **Dynamic Property Parsing**: Default values are parsed based on property types
4. **API Generation**: Function metadata is automatically converted to API responses

#### 3. Benefits
- **Zero Configuration**: New functions are automatically discovered
- **Self-Documenting**: Metadata is co-located with function implementations
- **Type Safety**: Property types and defaults are validated at compile time
- **Extensible**: Easy to add new function types without modifying core services

#### 4. Adding New Functions
To add a new function:
1. Create a class implementing `BucketFunction`
2. Add `@Component` annotation
3. **Required**: Add `@FunctionMetadata` annotation with function details
4. Function is automatically available via API

**Note**: All functions MUST have the `@FunctionMetadata` annotation. The system will throw an exception if a function is missing this annotation.

### Related Files
- `src/main/java/com/mahmoud/thoth/function/annotation/FunctionMetadata.java`
- `src/main/java/com/mahmoud/thoth/domain/service/FunctionInfoService.java`
- `src/main/java/com/mahmoud/thoth/function/impl/FileSizeLimitFunction.java`
- `src/main/java/com/mahmoud/thoth/function/impl/FileExtensionValidatorFunction.java`

---
*Last Updated: January 2025*
*Issues Resolved: Spring Data JDBC @Modifying queries with PGobject parameters, Dynamic function discovery system, Function assignment system evolution*

## Function Assignment System Evolution

### Overview
The function assignment system underwent a significant evolution from a complex polymorphic configuration system to a simplified, dynamic approach that better supports extensibility and maintainability.

### Initial Approach: Polymorphic Configuration System

#### Original Design
```java
// Complex polymorphic interface
public interface FunctionAssignConfig {
    String getType();
    int getOrder();
}

// Multiple concrete implementations
public class SizeLimitConfig implements FunctionAssignConfig { ... }
public class ExtensionValidatorConfig implements FunctionAssignConfig { ... }
```

#### Problems Encountered

1. **Complexity Overhead**
   - Required separate classes for each function type
   - Jackson polymorphic deserialization complexity
   - Difficult to add new function types

2. **Order Field Issues**
   - `order` field was always `0` despite being set in requests
   - Missing setter methods in concrete implementations
   - Inconsistent property mapping

3. **Database Persistence Problems**
   - Functions column was `null` after successful API calls
   - Spring Data JDBC `@Modifying` queries couldn't handle `PGobject` parameters
   - Required switching to `JdbcTemplate` approach

4. **Type Safety Issues**
   - `maxSizeBytes` property type mismatch (`Integer` stored, `Long` expected)
   - Required manual type conversion in `FunctionConfig.getProperty()`

### Final Solution: Simplified Dynamic System

#### New Design
```java
// Simple record-based configuration
public record FunctionConfig(
    String type,
    Map<String, Object> properties
) {
    public int getExecutionOrder() { ... }
    public <T> T getProperty(String key, Class<T> type) { ... }
    public <T> T getProperty(String key, Class<T> type, T defaultValue) { ... }
}
```

#### Key Improvements

1. **Simplified Configuration**
   - Single `FunctionConfig` record instead of multiple classes
   - Generic `Map<String, Object>` for properties
   - Automatic type conversion in `getProperty()` method

2. **Dynamic Function Discovery**
   - `@FunctionMetadata` annotation for function metadata
   - Spring auto-discovery of `BucketFunction` implementations
   - No need to modify core services when adding functions

3. **Robust Error Handling**
   - Specific exception types: `FunctionValidationException`, `FunctionConfigurationException`, `FunctionExecutionException`
   - Detailed error messages with context (bucket, object, function type)
   - Global exception handler for consistent error responses

4. **Clean Architecture**
   - Single `BucketFunction` interface for all functions
   - `BucketFunctionFactory` for function retrieval
   - `ExecuteBucketFunctionsUseCase` with clean, focused methods

### Why Functions Are Needed

#### Business Requirements
1. **File Validation**: Ensure uploaded files meet business rules
2. **Security**: Prevent malicious or inappropriate file uploads
3. **Compliance**: Enforce organizational policies
4. **Resource Management**: Control file sizes and types

#### Technical Benefits
1. **Extensibility**: Easy to add new validation rules
2. **Maintainability**: Clear separation of concerns
3. **Testability**: Each function can be tested independently
4. **Reusability**: Functions can be applied to multiple buckets
5. **Configuration**: Flexible configuration per bucket

#### Function Types Implemented

1. **File Size Limit Function**
   - Validates maximum file size
   - Configurable `maxSizeBytes` property
   - Prevents storage overflow

2. **File Extension Validator Function**
   - Validates allowed file extensions
   - Configurable `allowedExtensions` list
   - Security and compliance enforcement

### Lessons Learned

1. **Simplicity Over Complexity**: Simple, generic solutions often work better than complex polymorphic systems
2. **Type Safety**: Always handle type conversions explicitly
3. **Error Handling**: Provide specific, actionable error messages
4. **Database Operations**: Use `JdbcTemplate` for complex operations, Spring Data JDBC for simple CRUD
5. **Architecture**: Single responsibility principle makes code more maintainable

### Migration Path

1. **Phase 1**: Replace polymorphic system with `FunctionConfig` record
2. **Phase 2**: Implement dynamic function discovery
3. **Phase 3**: Add comprehensive error handling
4. **Phase 4**: Clean up and refactor for maintainability

### Related Files
- `src/main/java/com/mahmoud/thoth/function/config/FunctionConfig.java`
- `src/main/java/com/mahmoud/thoth/function/BucketFunction.java`
- `src/main/java/com/mahmoud/thoth/function/factory/BucketFunctionFactory.java`
- `src/main/java/com/mahmoud/thoth/domain/service/ExecuteBucketFunctionsUseCase.java`
- `src/main/java/com/mahmoud/thoth/shared/exception/GlobalExceptionHandler.java`
