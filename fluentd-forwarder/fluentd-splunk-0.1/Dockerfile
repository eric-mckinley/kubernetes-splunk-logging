FROM ruby:2.2

RUN apt-get update \
 && apt-get install gettext-base --yes \
 && apt-get clean all \
 && rm -rf /var/lib/apt/lists/*

RUN gem install fluentd

# Mix-in modules
RUN fluent-gem install fluent-mixin-config-placeholders
RUN fluent-gem install fluent-mixin-plaintextformatter

# Splunk output plugin for Fluent event collector.
RUN fluent-gem install fluent-plugin-splunk-http-eventcollector

# Enrich your fluentd events with Kubernetes metadata
RUN fluent-gem install fluent-plugin-kubernetes_metadata_filter

# Fluentd Output filter plugin to rewrite tags that matches specified attribute.
RUN fluent-gem install fluent-plugin-rewrite-tag-filter

COPY entrypoint.sh /entrypoint.sh

# Run the Fluentd service.
ENTRYPOINT ["/entrypoint.sh"]
