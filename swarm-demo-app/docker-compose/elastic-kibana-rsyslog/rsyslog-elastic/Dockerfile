FROM centos:6

COPY startup.sh /sbin/

RUN 	curl http://rpms.adiscon.com/v7-stable/rsyslog.repo >> /etc/yum.repos.d/rsyslog.repo && \
	    yum install -y rsyslog rsyslog-mmnormalize rsyslog-elasticsearch  && yum clean all -y

COPY 10_elasticsearch.conf /etc/rsyslog.d/
COPY rsyslog.conf /etc/rsyslog.conf
COPY *.rb /etc/rsyslog.d/
CMD /sbin/startup.sh
