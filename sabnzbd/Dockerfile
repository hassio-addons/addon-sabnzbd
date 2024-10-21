ARG BUILD_FROM=ghcr.io/hassio-addons/base:16.3.4
# hadolint ignore=DL3006
FROM ${BUILD_FROM}

# Set shell
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Setup base
ARG SABNZBD_VERSION=4.3.3
ARG UNRAR_VERSION=6.2.12
ARG PAR2CMDLINE_TURBO_VERSION=1.1.1
# hadolint ignore=DL3003,DL3042
RUN \
  apk add --no-cache --virtual .build-dependencies \
    autoconf=2.72-r0 \
    automake=1.16.5-r2 \
    build-base=0.5-r3 \
    linux-headers=6.6-r0 \
    musl-dev=1.2.5-r0 \
    py3-pip=24.0-r2 \
    python3-dev=3.12.7-r0 \
  \
  && apk add --no-cache \
    7zip=23.01-r0 \
    nginx=1.26.2-r0 \
    py3-setuptools=70.3.0-r0 \
    python3=3.12.7-r0 \
    unzip=6.0-r14 \
  \
  && curl -J -L -o /tmp/unrar.tar.gz \
      "https://www.rarlab.com/rar/unrarsrc-${UNRAR_VERSION}.tar.gz" \
  && tar zxvf \
      /tmp/unrar.tar.gz \
      -C /tmp \
  \  
  && cd /tmp/unrar \
  && make \
  && install -v -m755 unrar /usr/local/bin \
  && cd - \
  \
  && curl -J -L -o /tmp/par2cmdline-turbo.tar.gz \
      "https://github.com/animetosho/par2cmdline-turbo/archive/v${PAR2CMDLINE_TURBO_VERSION}.tar.gz" \
  && mkdir -p /tmp/par2cmdline-turbo \
  && tar zxvf \
      /tmp/par2cmdline-turbo.tar.gz \
      --strip 1 -C /tmp/par2cmdline-turbo \
  \
  && cd /tmp/par2cmdline-turbo \
  && ./automake.sh \
  && ./configure --prefix=/usr \
  && make \
  && make install \
  && cd - \
  \
  && curl -J -L -o /tmp/sabnzbd.tar.gz \
      "https://github.com/sabnzbd/sabnzbd/archive/${SABNZBD_VERSION}.tar.gz" \
  && tar zxvf \
      /tmp/sabnzbd.tar.gz \
      --strip 1 -C /opt \
  \
  && cd /opt \
  && pip install -r requirements.txt \
  && python tools/make_mo.py \
  \
  && find /usr/local \
    \( -type d -a -name test -o -name tests -o -name '__pycache__' \) \
    -o \( -type f -a -name '*.pyc' -o -name '*.pyo' \) \
    -exec rm -rf '{}' + \
  \
  && apk del --no-cache --purge .build-dependencies \
  && rm -f -r \
    /etc/nginx \
    /tmp/*

# Copy root filesystem
COPY rootfs /

# Build arguments
ARG BUILD_ARCH
ARG BUILD_DATE
ARG BUILD_DESCRIPTION
ARG BUILD_NAME
ARG BUILD_REF
ARG BUILD_REPOSITORY
ARG BUILD_VERSION

# Labels
LABEL \
    io.hass.name="${BUILD_NAME}" \
    io.hass.description="${BUILD_DESCRIPTION}" \
    io.hass.arch="${BUILD_ARCH}" \
    io.hass.type="addon" \
    io.hass.version=${BUILD_VERSION} \
    maintainer="Franck Nijhof <frenck@addons.community>" \
    org.opencontainers.image.title="${BUILD_NAME}" \
    org.opencontainers.image.description="${BUILD_DESCRIPTION}" \
    org.opencontainers.image.vendor="Home Assistant Community Add-ons" \
    org.opencontainers.image.authors="Franck Nijhof <frenck@addons.community>" \
    org.opencontainers.image.licenses="MIT" \
    org.opencontainers.image.url="https://addons.community" \
    org.opencontainers.image.source="https://github.com/${BUILD_REPOSITORY}" \
    org.opencontainers.image.documentation="https://github.com/${BUILD_REPOSITORY}/blob/main/README.md" \
    org.opencontainers.image.created=${BUILD_DATE} \
    org.opencontainers.image.revision=${BUILD_REF} \
    org.opencontainers.image.version=${BUILD_VERSION}
