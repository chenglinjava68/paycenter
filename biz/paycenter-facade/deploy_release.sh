#!/bin/sh
mvn clean deploy -Dmaven.test.skip=true -DaltDeploymentRepository=nexus-releases::default::http://192.168.2.126:8081/nexus/content/repositories/releases