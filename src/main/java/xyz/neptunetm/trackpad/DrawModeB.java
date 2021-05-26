package xyz.neptunetm.trackpad;

import org.lwjgl.opengl.GL11;

public class DrawModeB extends AbstractDrawMode {
    private final float centerWidth = .5f;
    private final float padding = .05f;

    public DrawModeB(Config config) {
        super(config);
    }

    @Override
    public void drawSteering(final float turn) {
        final float maxTurnWidth = (1f - centerWidth / 2f - padding);
        final float turnWidth = turn * maxTurnWidth;

        //left_arrow background
        GLBuilder.getBuilder().mode(GL11.GL_TRIANGLES)
                .color(0xEEEEEE, 1f)

                .vertex(-centerWidth / 2f, 0)
                .vertex(0, 1f - padding)
                .vertex(-centerWidth * 2f + padding, 0)

                .vertex(-centerWidth / 2f, 0)
                .vertex(0, padding - 1f)
                .vertex(-centerWidth * 2f + padding, 0)
                .end();
        // right arrow background
        GLBuilder.getBuilder().mode(GL11.GL_TRIANGLES)
                .color(0xEEEEEE, 1f)

                .vertex(centerWidth / 2f, 0)
                .vertex(0, 1f - padding)
                .vertex(centerWidth * 2f - padding, 0)

                .vertex(centerWidth / 2f, 0)
                .vertex(0, padding - 1f)
                .vertex(centerWidth * 2f - padding, 0)
                .end();

        // gas background
        GLBuilder.getBuilder().mode(GL11.GL_TRIANGLES)
                .color(0xEEEEEE, 1f)
                .vertex(-centerWidth / 2f, .001f)
                .vertex(centerWidth / 2f, .001f)
                .vertex(0, 1f - padding)
                .end();

        // brake background
        GLBuilder.getBuilder().mode(GL11.GL_TRIANGLES)
                .color(0xEEEEEE, 1f)
                .vertex(-centerWidth / 2f, -.001f)
                .vertex(centerWidth / 2f, -.001f)
                .vertex(0, -1f + padding)
                .end();

        if (turnWidth < 0) {

//            GLBuilder.getBuilder().mode(GL11.GL_QUADS)
//                    .color(config.steeringColor, 1f)
//                    .vertex(-centerWidth / 2f, 0)
//                    .vertex(0, 1f - padding)
//                    .vertex(-centerWidth / 2f + turnWidth, 0)
//                    .vertex(0, padding - 1f)
//                    .end();
            GLBuilder.getBuilder().mode(GL11.GL_TRIANGLES)
                    .color(config.steeringColor, 1f)

                    .vertex(-centerWidth / 2f, 0)
                    .vertex(0, 1f - padding)
                    .vertex(-centerWidth / 2f + turnWidth, 0)

                    .vertex(-centerWidth / 2f, 0)
                    .vertex(0, padding - 1f)
                    .vertex(-centerWidth / 2f + turnWidth, 0)
                    .end();


        } else if (turnWidth > 0) {
//            GLBuilder.getBuilder().mode(GL11.GL_QUADS)
//                    .color(config.steeringColor, 1f)
//                    .vertex(centerWidth / 2f, 0)
//                    .vertex(0, 1f - padding)
//                    .vertex(centerWidth / 2f + turnWidth, 0)
//                    .vertex(0, padding - 1f)
//                    .end();
            GLBuilder.getBuilder().mode(GL11.GL_TRIANGLES)
                    .color(config.steeringColor, 1f)

                    .vertex(centerWidth / 2f, 0)
                    .vertex(0, 1f - padding)
                    .vertex(centerWidth / 2f + turnWidth, 0)

                    .vertex(centerWidth / 2f, 0)
                    .vertex(0, padding - 1f)
                    .vertex(centerWidth / 2f + turnWidth, 0)
                    .end();

        }

        //WIREFRAMES
        //left-side
        GLBuilder.getBuilder().mode(GL11.GL_LINES)
                .color(config.steeringColor, 1f)
                .vertex(-centerWidth / 2f, 0)
                .vertex(0, 1f - padding)
                .vertex(0, 1f - padding)
                .vertex(padding - 1f, 0)
                .vertex(padding - 1f, 0)
                .vertex(0, padding - 1f)
                .vertex(0, padding - 1f)
                .vertex(-centerWidth / 2f, 0)
                .end();

        //right-side
        GLBuilder.getBuilder().mode(GL11.GL_LINES)
                .color(config.steeringColor, 1f)
                .vertex(centerWidth / 2f, 0)
                .vertex(0, 1f - padding)
                .vertex(0, 1f - padding)
                .vertex(1f - padding, 0)
                .vertex(1f - padding, 0)
                .vertex(0, padding - 1f)
                .vertex(0, padding - 1f)
                .vertex(centerWidth / 2f, 0)
                .end();
        if (config.controls.isAnalogueSteering) {
            //33%
            GLBuilder.getBuilder().mode(GL11.GL_LINES)
                    .color(60, 60, 60, 191)
                    .vertex(0, 1f - padding)
                    .vertex(centerWidth / 2f + maxTurnWidth / 3f, 0)
                    .vertex(centerWidth / 2f + maxTurnWidth / 3f, 0)
                    .vertex(0, padding - 1f)
                    .vertex(0, 1f - padding)
                    .vertex(-centerWidth / 2f - maxTurnWidth / 3f, 0)
                    .vertex(-centerWidth / 2f - maxTurnWidth / 3f, 0)
                    .vertex(0, padding - 1f)
                    .end();

            //67%
            GLBuilder.getBuilder().mode(GL11.GL_LINES)
                    .color(60, 60, 60, 191)
                    .vertex(0, 1f - padding)
                    .vertex(centerWidth / 2f + maxTurnWidth * 2 / 3f, 0)
                    .vertex(centerWidth / 2f + maxTurnWidth * 2 / 3f, 0)
                    .vertex(0, padding - 1f)
                    .vertex(0, 1f - padding)
                    .vertex(-centerWidth / 2f - maxTurnWidth * 2 / 3f, 0)
                    .vertex(-centerWidth / 2f - maxTurnWidth * 2 / 3f, 0)
                    .vertex(0, padding - 1f)
                    .end();
        }
    }

    @Override
    public void drawGas(final float gas) {
        GLBuilder.getBuilder().mode(GL11.GL_TRIANGLES)
                .color(config.gasColor, 1f)
                .vertex(-centerWidth / 2f, .001f)
                .vertex(centerWidth / 2f, .001f)
                .vertex(0, gas * (1f - padding))
                .end();

        GLBuilder.getBuilder().mode(GL11.GL_LINES)
                .color(config.gasColor, 1f)
                .vertex(-centerWidth / 2f, .001f)
                .vertex(centerWidth / 2f, .001f)
                .end();
    }

    @Override
    public void drawBrake(final float brake) {
        GLBuilder.getBuilder().mode(GL11.GL_TRIANGLES)
                .color(config.brakeColor, 1f)
                .vertex(-centerWidth / 2f, -.001f)
                .vertex(centerWidth / 2f, -.001f)
                .vertex(0, -brake * (1f - padding))
                .end();

        GLBuilder.getBuilder().mode(GL11.GL_LINES)
                .color(config.brakeColor, 1f)
                .vertex(-centerWidth / 2f, -.001f)
                .vertex(centerWidth / 2f, -.001f)
                .end();
    }
}
