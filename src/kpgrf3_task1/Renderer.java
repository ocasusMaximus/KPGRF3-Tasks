package kpgrf3_task1;
//package lvl2advanced.p01gui.p01simple;

import lwjglutils.*;
import org.lwjgl.glfw.*;
import transforms.*;


import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2019-09-02
 */
public class Renderer extends AbstractRenderer {

    private int shaderProgramMain, shaderProgramPost;
    private OGLBuffers buffersMain;
    private int viewLocation, projectionLocation, typeLocation, modelLocation, locLightPosition, locEyePosition;
    private Camera camera;
    private Camera camera2;
    private Mat4 projection;
    private OGLTexture2D textureForObjects;
    private OGLBuffers buffersPost;

    private boolean mousePressed = false;
    private double oldMx, oldMy;
    private OGLRenderTarget renderTarget;
    private OGLTexture2D.Viewer viewer;
    private float type = 0;
    boolean cameraType = true;
    private int button;

    int locTime;

    float time = 0;

    Mat4 model;
    Mat4 rotation;
    Mat4 translation;
    Vec3D lightPos;
    float colorType = 0;
    int colorTypeLoc;
    Vec3D eyePosition;
     int locHeight;
    boolean polygonMode = true;
    int shakeObjects;
    private int shake;
    private int locTimePostProc;


    @Override
    public void init() {
        OGLUtils.printOGLparameters();
        OGLUtils.printLWJLparameters();
        OGLUtils.printJAVAparameters();
        OGLUtils.shaderCheck();


        glClearColor(0.211f, 0.211f, 0.211f, 1f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        shaderProgramMain = ShaderUtils.loadProgram("/main");
        viewLocation = glGetUniformLocation(shaderProgramMain, "view");
        projectionLocation = glGetUniformLocation(shaderProgramMain, "projection");
        typeLocation = glGetUniformLocation(shaderProgramMain, "type");
        modelLocation = glGetUniformLocation(shaderProgramMain, "model");
        projectionLocation = glGetUniformLocation(shaderProgramMain, "projection");
        colorTypeLoc = glGetUniformLocation(shaderProgramMain, "colorType");
        locLightPosition = glGetUniformLocation(shaderProgramMain, "lightPosition");
        locEyePosition = glGetUniformLocation(shaderProgramMain, "eyePosition");
        locTime = glGetUniformLocation(shaderProgramMain, "time");


        shaderProgramPost = ShaderUtils.loadProgram("/post");

        locHeight = glGetUniformLocation(shaderProgramPost, "height");
        shakeObjects = glGetUniformLocation(shaderProgramPost, "shake");
        locTimePostProc = glGetUniformLocation(shaderProgramPost, "time");

        camera2 = new Camera()
                .withPosition(new Vec3D(0, 3, 2))
                .withAzimuth(6/ 4f * Math.PI)
                .withZenith(-1 / 5f * Math.PI);

        camera = new Camera()
                .withPosition(new Vec3D(3, 3, 2))
                .withAzimuth(5 / 4f * Math.PI)
                .withZenith(-1 / 5f * Math.PI);




        eyePosition = camera.getEye();
        //TODO: grid ktery se ohne do koule ve vertexshaderu, predat color


        lightPos  = new Vec3D(1, 3, 3);
        projection = new Mat4PerspRH(
                Math.PI / 3,
                height / (float) width,
                0.1,
                20
        );

        model = new Mat4Identity();
        rotation = new Mat4Identity();
        translation = new Mat4Identity();

//        float[] vertexBufferData = {
//                -1, -1,
//                1, 0,
//                0, 1,
//        };
//        int[] indexBufferData = {0, 1, 2};
//        OGLBuffers.Attrib[] attributes = {
//                new OGLBuffers.Attrib("inPosition", 2),
//        };
//        buffers = new OGLBuffers(vertexBufferData, attributes, indexBufferData);

        buffersMain = GridFactory.generateGridTriangleList(50, 50);
        buffersPost = GridFactory.generateGridTriangleList(2, 2);

        renderTarget = new OGLRenderTarget(1024, 1024);

        try {
            textureForObjects = new OGLTexture2D("./sky.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        viewer = new OGLTexture2D.Viewer();
        textRenderer = new OGLTextRenderer(width, height);
    }

    @Override
    public void display() {
        glEnable(GL_DEPTH_TEST);
        // text-renderer disables depth-test (z-buffer)
       if(polygonMode){
           glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
       } else {
           glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
       }
        renderMain();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        renderPostProcessing();

        glDisable(GL_DEPTH_TEST);

        viewer.view(textureForObjects, -1, -1, 0.5);
        viewer.view(renderTarget.getColorTexture(), -1, -0.5, 0.5);
        viewer.view(renderTarget.getDepthTexture(), -1, 0, 0.5);
        textRenderer.addStr2D(3, 15, "KPGRF3 TASK 1");
        textRenderer.addStr2D(3, 30, "JAN ZAHRADNÍK");
        textRenderer.addStr2D(3, 45, "FIM UHK 2021");
        textRenderer.addStr2D(width - 500, 15, "Controls: [WASD] Movement, [LMB] View rotation, [O,P] Change models, [C] Change camera");
        textRenderer.addStr2D(width - 500, 30, "[Q,E] Change projection, [K,L] Change texture, [J] Change polygon mode");
        textRenderer.addStr2D(width - 90, height - 3, " (c) PGRF UHK");
    }

    private void renderMain() {
        glUseProgram(shaderProgramMain);
        renderTarget.bind(); // render to texture

        if(cameraType){
            glUniformMatrix4fv(viewLocation, false, camera.getViewMatrix().floatArray());
            eyePosition = camera.getEye();
        } else {
            glUniformMatrix4fv(viewLocation, false, camera2.getViewMatrix().floatArray());
            eyePosition = camera2.getEye();
        }





        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//        glUniformMatrix4fv(viewLocation, false, camera.getViewMatrix().floatArray());
//        glUniformMatrix4fv(viewLocation, false, cameraTheSecond.getViewMatrix().floatArray());
        glUniformMatrix4fv(projectionLocation, false, projection.floatArray());
        glUniform3fv(locLightPosition, ToFloatArray.convert(lightPos));
        glUniform3fv(locEyePosition, ToFloatArray.convert(lightPos));

        glUniformMatrix4fv(modelLocation, false, model.floatArray());

        textureForObjects.bind(shaderProgramMain, "textureForObjects", 0);

        glUniform1f(typeLocation, type);
        //TODO: mohu zavolat znova a vykresli mi dalsi dokud nedam clear
        buffersMain.draw(GL_TRIANGLES, shaderProgramMain);
        glUniform1f(colorTypeLoc, colorType);




//        model
        time += 0.01;
        glUniform1f(locTime, time);
        //TODO: reflektorovy zdroj svetla spotDirection =-lightPosition;

//        buffersMain.draw(GL_TRIANGLES,shaderProgramMain);

//        glUniformMatrix4fv(typeLocation,1,GL_FALSE,(const GLfloat*) mvp);
    }

    private void renderPostProcessing() {
        glUseProgram(shaderProgramPost);
        glBindFramebuffer(GL_FRAMEBUFFER, 0); // render to window
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, width, height); // must reset back - render target is setting its own viewport

        renderTarget.getColorTexture().bind(shaderProgramPost, "textureRendered", 0);
        buffersPost.draw(GL_TRIANGLES, shaderProgramPost);
        glUniform1f(locHeight, height);
        time += 0.01;
        glUniform1f(locTimePostProc, time);
        glUniform1i(shakeObjects, shake);
        float move = (float) (1000.0 * 2*3.14159 * 0.75);  // 3/4 of a wave cycle per second

    }


    private final GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {

        @Override
        public void invoke(long window, double x, double y) {
            if (mousePressed) {
                if (button == GLFW_MOUSE_BUTTON_LEFT) {
                    if(cameraType){
                        camera = camera.addAzimuth(Math.PI / 2 * (oldMx - x) / width);
                        camera = camera.addZenith(Math.PI / 2 * (oldMy - y) / height);
                    } else {
                        camera2 = camera2.addAzimuth(Math.PI / 2 * (oldMx - x) / width);
                        camera2 = camera2.addZenith(Math.PI / 2 * (oldMy - y) / height);
                    }

                    oldMx = x;
                    oldMy = y;

                } else if (button == GLFW_MOUSE_BUTTON_RIGHT) {
                    //rotace
                    double rotX = (oldMx - x) / 200.0;
                    double rotY = (oldMy - y) / 200.0;
                    rotation = rotation.mul(new Mat4RotXYZ(rotX, 0, rotY));
                    model = rotation.mul(translation);
                    oldMx = x;
                    oldMy = y;
                } else if (button == GLFW_MOUSE_BUTTON_4) {
                    double movX = (oldMx - x) / 50;
                    double movY = (oldMy - y) / 50;
                    translation = translation.mul(new Mat4Transl(movX, movY, 0));
//                    rotation = new Mat4Identity();
                    //translace
//                    model = model.mul(translation).mul(rotation);
                    model = rotation.mul(translation);
                    oldMx = x;
                    oldMy = y;

                }


            }
        }
    };

    private final GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int buttonLocal, int action, int mods) {
            button = buttonLocal;
            double[] xPos = new double[1];
            double[] yPos = new double[1];
            glfwGetCursorPos(window, xPos, yPos);
            oldMx = xPos[0];
            oldMy = yPos[0];
            mousePressed = action == GLFW_PRESS;

        }
    };
    private final GLFWKeyCallback setKeyFallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                // We will detect this in our rendering loop
                glfwSetWindowShouldClose(window, true);
            }

            if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                switch (key) {
                    case GLFW_KEY_W:
                        camera = camera.forward(1);
                        break;
                    case GLFW_KEY_D:
                        camera = camera.right(1);
                        break;
                    case GLFW_KEY_S:
                        camera = camera.backward(1);
                        break;
                    case GLFW_KEY_A:
                        camera = camera.left(1);
                        break;
                    case GLFW_KEY_O:
                        if (type < 7) type += 1;
                        break;
                    case GLFW_KEY_P:
                        if (type > 0) type -= 1;
                        break;
                    case GLFW_KEY_Q:
                        //zmenit na persp
                        projection = new Mat4PerspRH(
                                Math.PI / 3,
                                height / (float) width,
                                0.1,
                                20
                        );

                        break;
                    case GLFW_KEY_E:
                        //zmeni na ortho
                        projection = new Mat4OrthoRH(2.5, 2.5, 0.1, 20);
                        break;
                    case GLFW_KEY_K:
                        if (colorType > 0) colorType -= 1;
                        break;
                    case GLFW_KEY_L:
                        if (colorType < 4) colorType += 1;
                        break;
                    case GLFW_KEY_C:
                        cameraType = !cameraType;
                        break;
                    case GLFW_KEY_J:
                        polygonMode = !polygonMode;
                        break;
                        //TODO: shake effect optional
                    case GLFW_KEY_H:
                        shake = 1;
                        break;
                    case GLFW_KEY_G:
                        shake = 0;
                        break;



                }
            }
        }
    };

    @Override
    public GLFWKeyCallback getKeyCallback() {
        return setKeyFallback;
    }


    @Override
    public GLFWCursorPosCallback getCursorCallback() {
        return cursorPosCallback;
    }

    @Override
    public GLFWMouseButtonCallback getMouseCallback() {
        return mouseButtonCallback;
    }

}
