# POC of Geo Load-Balancing Application

## Description

The following innovation idea, is about Geo Load-balancing and Geo Partitioned data.

This could be useful when it comes to the performances, since the user will be redirected to the closest location that application resides.

Another use case of Geo Load-balancing is to implement Geo-Partitioned data.

Geo-Partitioned data is keep the data in selected Geographical Locations by partitioning the data.

This is useful when storing PII (Personally Identifiable Information) data within the country to stick with the regulatory compliance requirements such as GDPR.

![Basic overview: How user is redirected based on user's location](https://i.imgur.com/IIlyVRM.png "Basic overview: How user is redirected based on user's location")

## User Redirection to Locations With and Without Geo Load-Balancers
### The Problem: Without Geo Load-Balancers

![The problem: How users are redirected random geo locations](https://i.imgur.com/ZJYQSyQ.png "The problem: How users are redirected random geo locations")

In this scenario,

- Users are redirected to locations randomly.
- They might redirected to regions with low latency even though users are far away from that region.
- The main issue with this implementation is the Database should be replicated in all the regions, thus violates the GDPR compliances.

### The Solution: With Geo Load-Balancers

![The solution: How users are redirected to geo location based applications](https://i.imgur.com/yD0oK8G.png "The solution: How users are redirected to geo location based applications")

In this scenario,

- Users are redirected based on their location, to the nearest region.
- Databases can be Geo-Parted as only regional data are stored within the region.
- This implementation is suitable for storing PII data as they will never leave the region that the users reside.

## Design
### Design Diagram to Test the Approach

![Design: How implementation was achieved using CloudFlare and AWS](https://i.imgur.com/4pPsO8e.png "Design: How implementation was achieved using CloudFlare and AWS")

In this scenario application is running inside EC2 instances. Each EC2 has it's own Database within the region.
- VPC peering enabled to route traffic between the two VPCs.
- Geographic routing feature in Cloudflare is used to route traffic based on the user's location.

Geographic routing consists of:
- Cloudflare loadbalncer to route traffic to origin pools.
- Origin pools which represent geographic locations and contains origin endpoints.


NOTE: Geographic routing can be done with AWS Route53 as well but our DNS is totally managed by Cloudflare and we wanted to explore the geographic routing feature in Cloudflare so we decided to use Cloudflare.


## Testing

To test the application with mock location we have used GeoTargetly online tool.

NOTE: To test this implementation actual user-services was not used due to privacy issues and time limitations. Instead sample application was used.

### Mock Scenario 1: US User Access the Application from USA 

![Scenario 1: Users are retrieved from USA](https://i.imgur.com/Ja6YFWF.png "Scenario 1: Users are retrieved from USA")

- This endpoint was implemented only for demonstration purposes
- Users are retrieved from the Database that reside within US Region

### Mock Scenario 2: Sri Lankan User Access the Application from Sri Lanka

![Scenario 2: Users are retrieved from Sydney](https://i.imgur.com/V9DCwtO.png "Scenario 2: Users are retrieved from Sydney")

- This endpoint was implemented only for demonstration purposes
- Users are retrieved from the Database that reside within Sydney Region
- Since Sri Lanka is more closer to Sydney than US region, data was received from Sydney

### Mock Scenario 3: US User Travel to Sri Lanka and Access the Application from Sri Lanka

![Scenario 3: US users are not retrieved from Sydney region](https://i.imgur.com/RPDOAzT.png "Scenario 3: US users are not retrieved from Sydney region")

- This endpoint was implemented only for demonstration purposes
- In this scenario user travels from USA to Sri Lanka and try to access his profile from Sri Lanka
- Since users are retrieved from the Database that reside within Sydney Region, US region user data are not available in Sydney

## Resolving the Issue with Scenario 3


### Two Resolutions
As solution for user accessing their data from other region,

- Solution 1: Application from Region 1 talks to application from Region 2 and get the user data
- Solution 2: Use Geo-Parted Database


### Solution 1: Application from Region 1 Talks to Application from Region 2 and Gets the User Data

![Solution 1: Application from Region 1 talks to application from Region 2 and get the user data](https://i.imgur.com/B7Twdrt.png "Solution 1: Application from Region 1 talks to application from Region 2 and get the user data")

In this solution,

- User from USA who travel to Sri Lanka is redirected to Sydney region
- Sydney region application find data in its Database
- If data is not found in its Database, application talks to other applications in other regions

### Solution 1: Testing with the Sample Application

![Solution 1: Application from region 1 talks to application from region 2 and gets the user data](https://i.imgur.com/39IggDU.png "Solution 1: Application from region 1 talks to application from region 2 and gets the user data")

### Solution 2: Use Geo-Parted Database

![Solution 2: Using Geo-Parted Database](https://i.imgur.com/xtF00Cr.png "Solution 2: Using Geo-Parted Database")

In this solution,

- Geo-Parted Databases can be used to distribute data among regions
- Multi region applications can be used if needed
- Region specific user data never leaves the region
- This also can be used to comply with GDPR compliance
- If users travel across regions, these Database can fetch data from other regions


## Examples of Geo-Parted Databases
We can manually partition already existing databases and have them placed in different countries or regions.

In order to do that we have re-architect already existing applications with complex logics.

#### YugabyteDB
- Open-Source DB
- Available under Apache 2.0 license
- row-level geo-partitioning capabilityes


#### CockroachDB
- Familiar to PostgreSQL tools
- Can be used as a service by their CockroachCloud
- Supports Kubernetes


## Multi-Region Applications vs. Geo-Parted Database
### Differences Between Them

| Multi-Region Applications                                                                   | Geo-Parted Databases                                                                  |
|---------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------|
| Have to re-architect application with complex logic to fetch data from other regions        | Need to setup new Database like YugabyteDB and transfer already existing data to them |
| Deploy the application to multiple regions                                                  | Have to partition the DB according to regions                                         |
| Need the support of Route53 or Cloudflare to route the users to region specific application | Need to host on AWS as theses databases not natively supported by AWS                 |
| Performance degradations if user travels from one region to the other                       | Performance degradations if user travels from one region to the other                 |

## Outcomes
- In order to support geo parted data multi-region applications or geo-parted databases can be used
- With multi region applications we have to re-architech the application
- YugabyteDB or CockroachDB are concrete implementations of geo parted databases 