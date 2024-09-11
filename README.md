# Serverless Java on AWS

This repository shows a small application using several AWS components with a serverless deployment model.
It is a (very) simplified file management solution. An Angular application uploads files to object storage (S3) and retrieves metadata from REST APIs (AWS Lambda). The backing data is stored in DynamoDB.

The following diagram shows some of the components that are used:

![quarkus-demo-app.drawio.png](quarkus-demo-app.drawio.png)

The project consists of:
 * `quarkus-app`: Quarkus on AWS Lambda
   * `serverless-java-rest-api` uses [quarkus-amazon-lambda-http](https://quarkus.io/guides/aws-lambda-http) to start an embedded resteasy container.
   * `serverless-java-s3-trigger` uses [quarkus-amazon-lambda](https://quarkus.io/guides/aws-lambda) as a generic Lambda trigger.
   * Both use [quarkus-amazon-dynamodb](https://docs.quarkiverse.io/quarkus-amazon-services/dev/amazon-dynamodb.html) to talk with a backing on-deman DynamoDB.
 * `angular-app`: Angular with AWS Amplify
   * Contains a simple web application that can call the Lambda functions and the S3 Object storage.
 * `aws-sam`: AWS CloudFormation YAML files for deploying the necessary infrastructure.

Since DynamoDB runs in provisioned mode and Lambdas terminate after execution, the running costs totals to 0€ for this demo (if you remain within the limits).

Some of the other supporting services are:
 * AWS CloudFormation: Allows definition of our AWS infrastructure as code.
 * AWS IAM: Used for access management between our AWS Services (e.g. AWS Lambda -> DynamoDB).
 * API Gateway: Required to have HTTP requests invoke the backing Lambda trigger.
 * AWS CloudFront: CDN for the Angular application. Allows running HTTPS for a static webapp stored in S3.
 * AWS Cognito: User authentication and authorization used by both the Angular frontend and Quarkus REST API.