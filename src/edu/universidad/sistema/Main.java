package edu.universidad.sistema;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Probar conexión
            if (ConexionBD.getConnection() != null) {
                System.out.println(" Conexión establecida correctamente");
            }

            // 2. Lanzar interfaz gráfica (Login)
            new Login();

        } catch (Exception e) {
            System.out.println(" Error en la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }
}




