ARG VARIANT=21-buster
FROM mcr.microsoft.com/vscode/devcontainers/java:${VARIANT}
SHELL ["/bin/bash", "-c"]
RUN source /usr/local/sdkman/bin/sdkman-init.sh; \
    sdk install maven; \
    sdk install springboot;
RUN apt-get update; \
    apt-get install -y docker.io;
    
#RUN sdk install quarkus
#RUN sdk install springboot

#ARG VARIANT=17-buster
#FROM mcr.microsoft.com/vscode/devcontainers/typescript-node:${VARIANT}
#RUN npm install -g @angular/cli

