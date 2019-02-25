#!/usr/bin/env bash

set -eufo pipefail

if [ -z ${HEC_TOKEN+x} ]; then
  echo >&2 "HEC_TOKEN is not defined"
  exit 1
fi

if [ -z ${HEC_ADDRESS+x} ]; then
  echo >&2 "HEC_ADDRESS is not defined"
  exit 1
fi

[ -z ${KUBERNETES_HOSTNAME+x} ] && export KUBERNETES_HOSTNAME=${HOSTNAME}

# mount the template in a different directory so it doesnt get overwritten
conftemplate="/etc/fluentd-template/fluentd.conf"
mkdir /etc/fluentd
conf="/etc/fluentd/fluentd.conf"
cat ${conftemplate} | envsubst '$HEC_TOKEN $HEC_ADDRESS $HEC_PROTOCOL $HEC_VERIFY_TLS $HEC_INDEX $KUBERNETES_HOSTNAME' > ${conf}
fluentd -c ${conf} -q "$@"