# DMatrix Project

This project, named DMatrix, is a Java-based application developed with Spring Boot. It is designed to perform various operations related to Geographical Information Systems (GIS).

## Project Structure

The project follows a standard Maven project structure. The main codebase is located under [``src/main/java/com/gis/``](command:_github.copilot.openRelativePath?%5B%7B%22scheme%22%3A%22file%22%2C%22authority%22%3A%22%22%2C%22path%22%3A%22%2Fhome%2Fshahin%2FDocuments%2FPhD%2FCode%2FAGILE2018%2Fsrc%2Fmain%2Fjava%2Fcom%2Fgis%2F%22%2C%22query%22%3A%22%22%2C%22fragment%22%3A%22%22%7D%5D "/home/shahin/Documents/PhD/Code/AGILE2018/src/main/java/com/gis/"). The project is divided into several packages, each serving a specific purpose:

- `config`: Contains configuration classes such as [`DmatrixConfiguration.java`](command:_github.copilot.openSymbolInFile?%5B%22src%2Fmain%2Fjava%2Fcom%2Fgis%2Fconfig%2FDmatrixConfiguration.java%22%2C%22DmatrixConfiguration.java%22%5D "src/main/java/com/gis/config/DmatrixConfiguration.java").
- `database`: Contains classes related to database operations.
- `gmap`: Contains classes like [`DMatrixCrawler.java`](command:_github.copilot.openSymbolInFile?%5B%22src%2Fmain%2Fjava%2Fcom%2Fgis%2Fgmap%2FDMatrixCrawler.java%22%2C%22DMatrixCrawler.java%22%5D "src/main/java/com/gis/gmap/DMatrixCrawler.java") that interact with Google Maps.
- `optimizer`: Contains classes for optimization operations, including [`OptimizationEngine.java`](command:_github.copilot.openSymbolInFile?%5B%22src%2Fmain%2Fjava%2Fcom%2Fgis%2Foptimizer%2FOptimizationEngine.java%22%2C%22OptimizationEngine.java%22%5D "src/main/java/com/gis/optimizer/OptimizationEngine.java").
- `util`: Contains utility classes like [`FileUtils.java`](command:_github.copilot.openSymbolInFile?%5B%22src%2Fmain%2Fjava%2Fcom%2Fgis%2Foptimizer%2Futil%2FFileUtils.java%22%2C%22FileUtils.java%22%5D "src/main/java/com/gis/optimizer/util/FileUtils.java") that provide helper methods for the project.

The project also includes unit tests under [``src/test/java/com/gis/``](command:_github.copilot.openRelativePath?%5B%7B%22scheme%22%3A%22file%22%2C%22authority%22%3A%22%22%2C%22path%22%3A%22%2Fhome%2Fshahin%2FDocuments%2FPhD%2FCode%2FAGILE2018%2Fsrc%2Ftest%2Fjava%2Fcom%2Fgis%2F%22%2C%22query%22%3A%22%22%2C%22fragment%22%3A%22%22%7D%5D "/home/shahin/Documents/PhD/Code/AGILE2018/src/test/java/com/gis/"), with tests for the main application in [`MainTests.java`](command:_github.copilot.openSymbolInFile?%5B%22src%2Ftest%2Fjava%2Fcom%2Fgis%2FMainTests.java%22%2C%22MainTests.java%22%5D "src/test/java/com/gis/MainTests.java").

## Building the Project

The project uses Maven for dependency management and build automation. You can build the project using the provided Maven wrapper scripts:

- On Unix/Linux/macOS: `./mvnw clean install`
- On Windows: `mvnw.cmd clean install`

## Dependencies

The project uses several dependencies, including:

- Spring Boot Starter Data JPA: Provides Spring Data JPA related APIs.
- Spring Boot Starter JDBC: Provides JDBC related APIs.
- Spring Boot Starter Test: Provides testing capabilities for Spring Boot applications.
- PostgreSQL JDBC Driver: Allows the application to connect to PostgreSQL databases.

## Configuration

The application's configuration is managed through the `application.properties` file located under [``src/main/resources/``](command:_github.copilot.openRelativePath?%5B%7B%22scheme%22%3A%22file%22%2C%22authority%22%3A%22%22%2C%22path%22%3A%22%2Fhome%2Fshahin%2FDocuments%2FPhD%2FCode%2FAGILE2018%2Fsrc%2Fmain%2Fresources%2F%22%2C%22query%22%3A%22%22%2C%22fragment%22%3A%22%22%7D%5D "/home/shahin/Documents/PhD/Code/AGILE2018/src/main/resources/").

## Output

The project includes a utility class, [`FileUtils`](command:_github.copilot.openSymbolInFile?%5B%22src%2Fmain%2Fjava%2Fcom%2Fgis%2Foptimizer%2Futil%2FFileUtils.java%22%2C%22FileUtils%22%5D "src/main/java/com/gis/optimizer/util/FileUtils.java"), which provides methods to output algorithm progress and aggregation data to JSON files.

## License

This project is licensed under the Apache License, Version 2.0. You can find more information about this license in the [``mvnw.cmd``](command:_github.copilot.openRelativePath?%5B%7B%22scheme%22%3A%22file%22%2C%22authority%22%3A%22%22%2C%22path%22%3A%22%2Fhome%2Fshahin%2FDocuments%2FPhD%2FCode%2FAGILE2018%2Fmvnw.cmd%22%2C%22query%22%3A%22%22%2C%22fragment%22%3A%22%22%7D%5D "/home/shahin/Documents/PhD/Code/AGILE2018/mvnw.cmd") file or at http://www.apache.org/licenses/LICENSE-2.0.
