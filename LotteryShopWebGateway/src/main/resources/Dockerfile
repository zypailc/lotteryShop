FROM java:8
  # 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
echo 'Asia/Shanghai' >/etc/timezone

ADD application.jar application-1.0.0.jar

RUN bash -c 'touch /application-1.0.0.jar'

EXPOSE 8080

ENTRYPOINT ["java","-jar","/application-1.0.0.jar","-Duser.timezone=Asia/Shanghai"]