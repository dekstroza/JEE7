FROM centos:6

COPY startup.sh /sbin/


RUN yum install -y rsyslog7-elasticsearch.x86_64 && yum clean all -y
COPY 10_elasticsearch.conf /etc/rsyslog.d/

CMD /sbin/startup.sh

EXPOSE 514

