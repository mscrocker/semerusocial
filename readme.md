# SemeruSocial Friend Match App

This is a [Spring MVC](https://spring.io) project using [Thymeleaf](http://www.thymeleaf.org/) templates for the views, created with the use of the [Spring MVC with Thymeleaf Maven Archetype](https://github.com/Bernardo-MG/spring-mvc-thymeleaf-maven-archetype). It will ease the development of new Spring MVC projects, setting it up for CI through the use of two free services: [Github](https://github.com/) and [Jenkins](https://jenkins.io).


## Features

The project by default comes with a useful series of features:

- Preconfigured POM to begin developing a new [Spring MVC](https://spring.io) project.
- Initial sample project including working persistence and exception handling
- Integration with [Thymeleaf](http://www.thymeleaf.org/) for the view templates.
- Using [Bootstrap](http://getbootstrap.com/) for the UI.
- Using [Liquibase](http://www.liquibase.org/) for database versioning.
- Unit and integration tests suites ready to be run with [JUnit](http://junit.org) just by using the Maven test and verify commands.
- A Maven site, using the [Docs Maven Skin](https://github.com/Bernardo-MG/docs-maven-skin), to contain the documentation, the Javadocs and several reports.
- A bunch of useful files, such as readme, gitignore and gitattributes.


```
mvn verify site -P h2
```

The verify phase is required, otherwise some of the reports won't be generated.

## Usage

The application is coded in Java, using Maven to manage the project.

### Prerequisites

The project has been tested on the following Java versions:
* JDK 11
* Apache Maven 3.6.2

All other dependencies are handled through Maven, and noted in the included POM file.

### Profiles

Maven profiles are included for setting up the database and an embedded server.

| Profile  | Database              |
|----------|-----------------------|
| h2       | H2 in-memory database |
| mysql    | MySQL database        |
| postgres | PostgreSQL database   |

### Installing

The project can be installed by creating the war file and deploying it into a server.

### Running

To run the project locally in an embedded server just use the following Maven command for deploying to Tomcat9 with an H2 in-memory database:

```
mvn package -P h2
mvn org.codehaus.cargo:cargo-maven2-plugin:run --projects frontend
```

With this the project will be accessible at [http://localhost:8080/semerusocial].

### Running the tests

The project requires a database and a server for being able to run the integration tests.

Just like running the project, an embedded server with an in-memory database can be used:

```
mvn verify -P h2
```

### Packaging the WAR

When creating the WAR file the database connection credentials should be set manually:

```
mvn package -P mysql -Ddatabase.username=[username] -Ddatabase.password=[password] -Ddatabase.url=[DB url]
```

Otherwise the project will try to use the default testing values.

## Collaborate

Any kind of help with the project will be well received, and there are two main ways to give such help:

- Reporting errors and asking for extensions through the issues management
- or forking the repository and extending the project

### Issues management

Issues are managed at the GitHub [project issues tracker][issues], where any Github user may report bugs or ask for new features.

### Getting the code

If you wish to fork or modify the code, visit the [GitHub project page][scm], where the latest versions are always kept. Check the 'master' branch for the latest release, and the 'develop' for the current, and stable, development version.

## License

The project has been released under the [MIT License][license].


[issues]: https://github.com/mscrocker/semerusocial/issues
[license]: http://www.opensource.org/licenses/mit-license.php
[scm]: https://github.com/mscrocker/semerusocial