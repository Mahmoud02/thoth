package com.mahmoud.thoth.api.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class BucketFunctionApiDocs {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Add functions to a bucket",
        description = "Assigns multiple functions (like size limit or extension validation) to a specified bucket using dynamic configuration",
        requestBody = @RequestBody(
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Multiple Functions Example",
                        summary = "Adding size limit and extension validation to a bucket",
                        value = """
                        {
                          "bucketId": 1,
                          "configs": [
                            {
                              "type": "SIZE_LIMIT",
                              "properties": {
                                "maxSizeBytes": 10485760,
                                "order": 1
                              }
                            },
                            {
                              "type": "EXTENSION_VALIDATOR",
                              "properties": {
                                "allowedExtensions": ["jpg", "png", "gif"],
                                "order": 2
                              }
                            }
                          ]
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Single Function Example",
                        summary = "Adding just a size limit to a bucket",
                        value = """
                        {
                          "bucketId": 1,
                          "configs": [
                            {
                              "type": "SIZE_LIMIT",
                              "properties": {
                                "maxSizeBytes": 5242880,
                                "order": 1
                              }
                            }
                          ]
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Function with Custom Properties",
                        summary = "Adding a function with custom configuration properties",
                        value = """
                        {
                          "bucketId": 2,
                          "configs": [
                            {
                              "type": "SIZE_LIMIT",
                              "properties": {
                                "maxSizeBytes": 20971520,
                                "order": 1,
                                "customProperty": "value"
                              }
                            }
                          ]
                        }
                        """
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Functions successfully added to bucket",
                content = @Content(
                    mediaType = "application/json",
                    examples = {
                        @ExampleObject(
                            name = "Success Response",
                            summary = "Response when functions are successfully added",
                            value = """
                            {
                              "bucketId": 1,
                              "functionsAdded": 2,
                              "configValues": [
                                {
                                  "type": "SIZE_LIMIT",
                                  "properties": {
                                    "maxSizeBytes": 10485760,
                                    "order": 1
                                  }
                                },
                                {
                                  "type": "EXTENSION_VALIDATOR",
                                  "properties": {
                                    "allowedExtensions": ["jpg", "png", "gif"],
                                    "order": 2
                                  }
                                }
                              ]
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request parameters or function configuration errors"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Bucket not found"
            )
        }
    )
    public @interface AddBucketFunctionOp {
    }
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Remove a function from a bucket",
        description = "Removes the specified function from a bucket by bucket ID and function type",
        parameters = {
            @Parameter(name = "buketId", description = "ID of the bucket", example = "1"),
            @Parameter(name = "type", description = "Type of function to remove", example = "SIZE_LIMIT")
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Function successfully removed from bucket"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Bucket or function not found"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid function type or bucket ID"
            )
        }
    )
    public @interface RemoveBucketFunctionOp {
    }
}