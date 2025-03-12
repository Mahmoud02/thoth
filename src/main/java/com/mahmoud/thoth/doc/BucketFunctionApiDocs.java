package com.mahmoud.thoth.doc;

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
        description = "Assigns multiple functions (like size limit or extension validation) to a specified bucket",
        requestBody = @RequestBody(
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Multiple Functions Example",
                        summary = "Adding size limit and extension validation to a bucket",
                        value = """
                        {
                          "bucketName": "my-documents",
                          "configs": [
                            {
                              "type": "SIZE_LIMIT",
                              "maxSizeBytes": 10485760
                            },
                            {
                              "type": "EXTENSION_VALIDATOR",
                              "allowedExtensions": ["jpg", "png", "gif"]
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
                          "bucketName": "my-documents",
                          "configs": [
                            {
                              "type": "SIZE_LIMIT",
                              "maxSizeBytes": 10485760
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
                description = "Functions successfully added to bucket"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request parameters"
            )
        }
    )
    public @interface AddBucketFunctionOp {
    }
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Remove a function from a bucket",
        description = "Removes the specified function from a bucket",
        parameters = {
            @Parameter(name = "bucketName", description = "Name of the bucket", example = "my-documents"),
            @Parameter(name = "type", description = "Type of function to remove", example = "SIZE_LIMIT")
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Function successfully removed"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Bucket or function not found"
            )
        }
    )
    public @interface RemoveBucketFunctionOp {
    }
}