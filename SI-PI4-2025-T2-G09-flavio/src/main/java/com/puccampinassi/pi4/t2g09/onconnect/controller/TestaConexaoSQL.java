package com.puccampinassi.pi4.t2g09.onconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class TestaConexaoSQL implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(TestaConexaoSQL.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Testando conexão");
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("Conectado ao banco de teste: " + conn.getCatalog());
            System.out.println("URL: " + conn.getMetaData().getURL());
            System.out.println("Usuário: " + conn.getMetaData().getUserName());
            System.out.println("Versão do MySQL: " + conn.getMetaData().getDatabaseProductVersion());

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT NOW() AS data_hora")) {
                if (rs.next()) {
                    System.out.println("MySQL respondeu: " + rs.getString("data_hora"));
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao conectar:");
            e.printStackTrace();
        }
    }
}
