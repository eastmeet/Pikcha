package com.main36.pikcha.global.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Profile("server")
public class SshMySqlConnectionConfig {

    @Value("${ssh.host}")
    private String sshHost;

    @Value("${ssh.port}")
    private int sshPort;

    @Value("${ssh.user}")
    private String sshUser;

    @Value("${ssh.pem}")
    private String pemKey;

    @Value("${ssh.local.port}")
    private int localPort;

    @Value("${ssh.remote.host}")
    private String remoteHost;

    @Value("${ssh.remote.port}")
    private int remotePort;

    @Value("${ssh.remote.username}")
    private String userName;

    @Value("${ssh.remote.password}")
    private String password;

    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private Session session = null;

    @Bean
    public Session sshSession() throws JSchException {
        JSch jsch = new JSch();
        jsch.addIdentity(pemKey);
        session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        session.setPortForwardingL(localPort, remoteHost, remotePort);
        return session;
    }

    @Bean
    public DataSource dataSource(Session session) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://localhost:%d/pikcha", localPort));
        hikariConfig.setUsername(userName);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(JDBC_DRIVER);
        return new HikariDataSource(hikariConfig);
    }

    @PreDestroy
    public void destroy() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }

    }
}