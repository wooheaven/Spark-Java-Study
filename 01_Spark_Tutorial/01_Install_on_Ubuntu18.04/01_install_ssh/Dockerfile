FROM ubuntu:18.04

RUN sed -i 's/archive.ubuntu.com/ftp.daumkakao.com/g' /etc/apt/sources.list && \
    sed -i 's/security.ubuntu.com/ftp.daumkakao.com/g' /etc/apt/sources.list
RUN grep -v "#" /etc/apt/sources.list | grep -v '^$' 

RUN apt-get clean && \
    apt-get update

RUN apt-get -y install software-properties-common \
    vim \
    tree \
    openssh-server \
    net-tools \
    iputils-ping \
    zip unzip \
    && rm -rf /var/lib/apt/lists/*

RUN sed -i 's/#PasswordAuthentication yes/PasswordAuthentication no/' /etc/ssh/sshd_config
RUN cat -n /etc/ssh/sshd_config | head -57 | tail -3

RUN service ssh start && \
    ssh-keygen -q -t rsa -N '' -f /root/.ssh/id_rsa && \
    cd /root/.ssh/ && \
    cp id_rsa.pub authorized_keys && \
    chmod 600 authorized_keys && \
    touch /root/.ssh/known_hosts && \
    ssh-keyscan -t rsa localhost >> /root/.ssh/known_hosts && \
    chmod 600 known_hosts && \
    ls -als /root/.ssh && \
    service ssh stop

ADD 99_Utility/*.sh /root/
RUN chmod +x /root/*.sh

WORKDIR /root
