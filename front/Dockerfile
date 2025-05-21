FROM openjdk:17-slim

# Installer quelques dépendances nécessaires (curl, unzip…)
RUN apt-get update && apt-get install -y wget unzip

# Installer Android SDK
ENV ANDROID_SDK_ROOT /sdk
RUN mkdir -p $ANDROID_SDK_ROOT

RUN wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O tools.zip \
    && unzip tools.zip -d $ANDROID_SDK_ROOT/cmdline-tools \
    && mv $ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools $ANDROID_SDK_ROOT/cmdline-tools/latest \
    && rm tools.zip

# Ajouter SDK tools au PATH
ENV PATH="${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools"

# Accepter les licences et installer les plateformes nécessaires
RUN yes | sdkmanager --licenses
RUN sdkmanager "platform-tools" "platforms;android-33" "build-tools;33.0.2"

# Copier ton projet dans le conteneur
WORKDIR /app
COPY . /app

# Build du projet
RUN ./gradlew assembleDebug

CMD ["./gradlew", "assembleDebug"]
