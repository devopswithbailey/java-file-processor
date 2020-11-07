FROM openjdk:8u181-jdk

ENV UID=${UID:-"483225"} \
    USER=${USER:-"trsappid"} \
    GID=${GID:-"16787"} \
    GROUP_NAME=${GROUP_NAME:-"trsgp"} \
    HOME=${HOME:-"/home/trsappid"} \
    ARTIFACT_NAME=${ARTIFACT_NAME:-"tokenization*.jar"} \
    JVM_OPTIONS=${JVM_OPTIONS:-"-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=2 -XshowSettings:vm"}

RUN groupadd -g  ${GID} ${GROUP_NAME} \
    && useradd -m -u ${UID}  -g ${GID} -d /home/${USER} -s /bin/sh ${USER} \
    && chown -R ${USER}:${GROUP_NAME} /home/${USER}
WORKDIR /home/${USER}

COPY ./target/*.jar ./TokenizationServiceApp.jar

USER ${UID}

ENTRYPOINT ["/bin/bash","-c","java ${JVM_OPTIONS} -jar ./TokenizationServiceApp.jar"]