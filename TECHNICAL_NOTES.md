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
*Last Updated: $(date)*
*Issues Resolved: Spring Data JDBC @Modifying queries with PGobject parameters, Dynamic function discovery system*
