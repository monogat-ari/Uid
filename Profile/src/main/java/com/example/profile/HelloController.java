package com.example.profile;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HelloController {

    @FXML
    private Canvas canvas;

    private final String[] labels = {"Attacco", "Difesa", "Velocità"};
    private final double[] values = {80, 60, 50}; // 0–100

    Font minecraftFont = Font.loadFont(
            getClass().getResourceAsStream("/com/example/profile/Minecraft.ttf"), 13
    );

    @FXML
    public void initialize() {
        drawSpiderChart();
    }

    private void drawSpiderChart() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        double centerX = width / 2;
        double centerY = height / 2;

        // Calcolo dinamico del raggio (evita di sforare)
        double padding = 40; // spazio per le etichette
        double radius = Math.min(width, height) / 2 - padding;

        int n = values.length;
        double angleStep = 2 * Math.PI / n;

        // Sfondo
        gc.setFill(Color.rgb(240, 240, 255, 0));
        gc.fillRect(0, 0, width, height);

        // Griglia
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);
        for (int i = 1; i <= 3; i++) {
            double r = radius * i / 3;
            gc.beginPath();
            for (int j = 0; j < n; j++) {
                double angle = j * angleStep;
                double x = centerX + r * Math.sin(angle);
                double y = centerY - r * Math.cos(angle);
                if (j == 0) gc.moveTo(x, y);
                else gc.lineTo(x, y);
            }
            gc.closePath();
            gc.stroke();
        }

        // Poligono dei valori
        gc.setStroke(Color.DODGERBLUE);
        gc.setFill(Color.rgb(30, 144, 255, 0.4));
        gc.setLineWidth(2);
        gc.beginPath();
        for (int i = 0; i < n; i++) {
            double r = radius * (values[i] / 100.0);
            double angle = i * angleStep;
            double x = centerX + r * Math.sin(angle);
            double y = centerY - r * Math.cos(angle);
            if (i == 0) gc.moveTo(x, y);
            else gc.lineTo(x, y);
        }
        gc.closePath();
        gc.fill();
        gc.stroke();

        // Etichette
        gc.setFill(Color.BLACK);
        gc.setFont(minecraftFont);
        for (int i = 0; i < n; i++) {
            double angle = i * angleStep;
            double labelRadius = radius + 20; // spazio tra bordo e testo
            double x = centerX + labelRadius * Math.sin(angle);
            double y = centerY - labelRadius * Math.cos(angle);
            gc.fillText(labels[i], x - 20, y);
        }
    }

}
