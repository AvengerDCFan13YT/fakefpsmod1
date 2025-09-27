package com.example.fakefps;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Random;

public class FakeFpsMod implements ClientModInitializer {

    private final Random random = new Random();
    private int fakeFps = 800 + random.nextInt(401);
    private int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        // Update fake FPS once per second
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            tickCounter++;
            if (tickCounter >= 20) {
                fakeFps = 800 + random.nextInt(401);
                tickCounter = 0;
            }
        });

        HudRenderCallback.EVENT.register((MatrixStack matrices, float tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.textRenderer == null) return;

            String text = "FPS: " + fakeFps;

            // Measure text width to right-align
            int width = client.getWindow().getScaledWidth();
            int textWidth = client.textRenderer.getWidth(text);

            int x = width - textWidth - 5;
            int y = 5;

            client.textRenderer.drawWithShadow(matrices, text, x, y, 0xFFFFFF);
        });
    }
}
