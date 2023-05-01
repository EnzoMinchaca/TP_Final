/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tpfinal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import top.jfunc.json.impl.JSONArray;
import top.jfunc.json.impl.JSONObject;

/**
 *
 * @author Enzo
 */
public class TPFinal {
    
    private static Scanner sc = new Scanner(System.in).useDelimiter("\n");
    private static Conexion conexion = new Conexion();

    public static void main(String[] args) throws SQLException, JsonProcessingException, IOException {

        int input;
        
        do {
            input = 0;
            System.out.println("Elija una opción");
            System.out.println("1-Crear materia");
            System.out.println("2-Crear alumno");
            System.out.println("3-Inscribir alumno");
            try {
                input = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Ingresa un número");
            }
            switch(input) {
                case 1:
                  crearMateria();
                  break;
                case 2:
                  crearAlumno();
                break;
                case 3:
                  verificacion();
                break;
                default:
                  System.out.println("Ingrese un número válido");
             }
        } while (input != 4); {
            System.out.println("Chau");
        };
        
    }
    
    public static void crearMateria() throws SQLException{
    
        Materia materia = new Materia();
        
        System.out.println("¿Cuál es el nombre de la materia?");
        String nombre = sc.next();
        materia.setNombre(nombre);

        System.out.println("¿Cuantas correlativas tiene la materia?");
        int numero = sc.nextInt();

        System.out.println("¿Que materias serán sus correlativas?");
        ArrayList<String> correlativas = new ArrayList<>();

        String input;
        for (int i = 0; i < numero; i++) {
            System.out.println("Materia "+(i+1)+":");
            input = sc.next();
            correlativas.add(input);
        }
        
        String correlativasJson = new Gson().toJson(correlativas);
        conexion.estableceConexion();
        Statement stmt = conexion.conectar.createStatement();
        stmt.executeUpdate("INSERT INTO materias VALUES(\"" + nombre + "\",'" + correlativasJson + "');");
        System.out.println("Materia creada exitosamente!\n");
        conexion.cerrarConnection();
    }

    public static void traerDatos() throws SQLException, JsonProcessingException, IOException {

        Materia materia = new Materia();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        
        HashMap<String, ArrayList<String>> hmMaterias = new HashMap<>();

        conexion.estableceConexion();
        Statement stmt = conexion.conectar.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM materias");

        while (rs.next()) {

            materia = new Materia(rs.getString("nombre"));
            String jsonText = objectMapper.writeValueAsString(rs.getString("correlativas"));
            ArrayList<String> nombreCorrelativas = objectMapper.readValue(jsonText, ArrayList.class);
            materia.setCorrelativas(nombreCorrelativas);
            hmMaterias.put(materia.getNombre(), materia.getCorrelativas());

        }
        conexion.cerrarConnection();
        System.out.println(hmMaterias);
    }
    
    public static void crearAlumno() throws SQLException, JsonProcessingException, IOException {
        
        Alumno alumno = new Alumno();
        
        System.out.println("¿Cuál es el nombre del alumno?");
        String nombre = sc.next();
        alumno.setNombre(nombre);
         
        String valor;
        int legajo;
        do {
            System.out.println("¿Cuál es el legajo del alumno?");
            legajo = sc.nextInt();
            valor = Integer.toString(legajo);
            alumno.setLegajo(legajo);
            if(valor.length() != 5) {
                System.out.println("Debe tener 5 dígitos");
            }
        } while(valor.length() != 5 ); {
            
        }
        
        System.out.println("¿Cuantas materias tiene aprobadas?");
        int numero = sc.nextInt();
        System.out.println("Escriba dichas materias");
        ArrayList<String> materiasAprobadas = new ArrayList<>();

        String input;
        for (int i = 0; i < numero; i++) {
            System.out.println("Materia "+(i+1)+":");
            input = sc.next();
            materiasAprobadas.add(input);
        }
        
        String materiasAprobadasJson = new Gson().toJson(materiasAprobadas);
        conexion.estableceConexion();
        Statement stmt = conexion.conectar.createStatement();
        stmt.executeUpdate("INSERT INTO alumnos VALUES(\"" + nombre + "\", "+legajo+",'" + materiasAprobadasJson + "');");
        System.out.println("Alumno creado exitosamente!\n");
        conexion.cerrarConnection();
    }
    
    public static void verificacion() throws SQLException, JsonProcessingException, IOException {

        System.out.println("Que alumno quiere inscribir? (Legajo)");
        int legajoAlumno = sc.nextInt();
        System.out.println("A que materia se quiere inscribir?");
        String nombreMateria = sc.next();

        Inscripcion insc = new Inscripcion();
        insc.aprobada(traerDatosAlumno(legajoAlumno), traerDatosMateria(nombreMateria));
    }

    public static Alumno traerDatosAlumno(int legajo) throws SQLException, JsonProcessingException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        conexion.estableceConexion();
        Statement stmt = conexion.conectar.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM alumnos WHERE legajo=" + legajo + ";");
        rs.next();
        Alumno alumno = new Alumno(rs.getString("nombre"), rs.getInt("legajo"));

//        String jsonText = mapper.writeValueAsString(rs.getString("correlativas"));
//        ArrayList<String> nombreCorrelativas = mapper.readValue(jsonText, ArrayList.class);
//        alumno.setMateriasAprobadas(nombreCorrelativas);
        //alumno.setMateriasAprobadas(mapper.readValue(mapper.writeValueAsString(rs.getString("correlativas")), ArrayList.class));
        alumno.setMateriasAprobadas(mapper.readValue(rs.getString("materias_aprobadas"), ArrayList.class));

        alumno.setMateriasAprobadas(mapper.readValue(mapper.writeValueAsString(rs.getString("materias_aprobadas")), ArrayList.class));
        
        conexion.cerrarConnection();

        return alumno;
    }

    public static Materia traerDatosMateria(String nombre) throws SQLException, JsonProcessingException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        conexion.estableceConexion();
        Statement stmt = conexion.conectar.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM materias WHERE nombre=\"" + nombre + "\";");
        rs.next();
        Materia materia = new Materia(rs.getString("nombre"));

//        materia.setCorrelativas(mapper.readValue(mapper.writeValueAsString(rs.getString("correlativas")), ArrayList.class));
        materia.setCorrelativas(mapper.readValue(rs.getString("correlativas"), ArrayList.class));

        materia.setCorrelativas(mapper.readValue(mapper.writeValueAsString(rs.getString("correlativas")), ArrayList.class));

        conexion.cerrarConnection();

        return materia;
    }
}
