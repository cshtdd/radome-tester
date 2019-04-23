# Radome Tester  

## Design Considerations  

The system moves two motors:
- Theta (X coordinates): Rotates the radeon horizontally. This motor is oriented vertically. It is glued to the floor. Values should range from 180 to 360 degrees.  
- Phi (Y coordinates): Rotates the radeon vertically. This motor is oriented horizontally. It is glued to a columnt. Values should range from 0 to 180 degrees.  

## Development  

### Select Java 11  

```bash
sdk use java 11.0.1-open
```

### Packaging the Application  

```bash
mvn clean package
```

### Run the Application  

```bash
./target/radome-tester-*.jar
```

### Deployment Prerequisite  

Create the `hosts.ini` inventory file  

```
[all]
192.168.1.2  ansible_connection=ssh  ansible_user=pi  become_user=pi
```

### Build and deploy  

```bash
sh build-deploy-run.sh
```

### Deploy the application  

```bash
sh deploy.sh
```

### Application logs  

```bash
grep "radome-tester" /var/log/syslog
```