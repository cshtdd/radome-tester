# Radome Tester  

## Packaging the Application  

```bash
mvn clean package
```

## Run the Application  

```bash
./target/radome-tester-*.jar
```

## Deployment Prerequisite  

Create the `hosts.ini` inventory file  

```
[all]
192.168.1.2  ansible_connection=ssh  ansible_user=pi  become_user=pi
```

## Deploy the application  

```bash
sh deploy.sh
```

## Application logs  

```bash
grep "radome-tester" /var/log/syslog
```