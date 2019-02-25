ifeq ($(origin VERSION), undefined)
	VERSION := $(shell git describe --tags --always --dirty)
endif

build:
	docker build -t apprenda/fluentd-splunk .
	docker tag apprenda/fluentd-splunk apprenda/fluentd-splunk:$(VERSION)

push: build
	docker push apprenda/fluentd-splunk:$(VERSION)
	docker push apprenda/fluentd-splunk:latest