package p01simple;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryStack;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.IIOByteBuffer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class LwjglWindow {

    private static int WIDTH = 600;
    private static int HEIGHT = 400;

    private long window; // the window handle
    private final AbstractRenderer renderer;

    private static boolean DEBUG = false;

    static {
        if (DEBUG) {
            System.setProperty("org.lwjgl.util.Debug", "true");
            System.setProperty("org.lwjgl.util.NoChecks", "false");
            System.setProperty("org.lwjgl.util.DebugLoader", "true");
            System.setProperty("org.lwjgl.util.DebugAllocator", "true");
            System.setProperty("org.lwjgl.util.DebugStack", "true");
            Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
        }
    }

    public LwjglWindow(AbstractRenderer renderer) throws IOException {
        this(WIDTH, HEIGHT, renderer, false);
    }

    public LwjglWindow(AbstractRenderer renderer, boolean debug) throws IOException {
        this(WIDTH, HEIGHT, renderer, debug);
    }

    public LwjglWindow(int width, int height, AbstractRenderer renderer, boolean debug) throws IOException {
        this.renderer = renderer;
        DEBUG = debug;
        WIDTH = width;
        HEIGHT = height;
        if (DEBUG) {
            System.err.println("Run in debugging mode");
        }
        run();
    }

    private void run() throws IOException {
        init();
        loop();
        renderer.dispose();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() throws IOException {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

//        String text = renderer.getClass().getName();
        String text = "KPGRF3 Task 1";
//        text = text.substring(0, text.lastIndexOf('.'));
        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, text, NULL, NULL);

        ClassLoader classLoader = getClass().getClassLoader();

//        Image icon = Importer.extractImageFromImagePath(classLoader.getResource("icon.png").getFile());
//        assert icon != null;
//        GLFWImage.Buffer gb = GLFWImage.malloc(icon.getHeight()*icon.getWidth()*4);
//        byte[] iconData = ((DataBufferByte) icon.getRaster().getDataBuffer()).getData();
//        ByteBuffer ib = createByteBuffer(iconData.length);
//        GLFWImage iconGI = GLFWImage.create().set(icon.getWidth(), icon.getHeight(), ib);
//        gb.put(0, iconGI);


//        glfwSetWindowIcon(window, gb);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, renderer.getKeyCallback());
        glfwSetWindowSizeCallback(window, renderer.getWsCallback());
        glfwSetMouseButtonCallback(window, renderer.getMouseCallback());
        glfwSetCursorPosCallback(window, renderer.getCursorCallback());
        glfwSetScrollCallback(window, renderer.getScrollCallback());

        if (DEBUG)
            glfwSetErrorCallback(new GLFWErrorCallback() {
                final GLFWErrorCallback delegate = GLFWErrorCallback.createPrint(System.err);

                @Override
                public void invoke(int error, long description) {
                    if (error == GLFW_VERSION_UNAVAILABLE)
                        System.err.println("GLFW_VERSION_UNAVAILABLE: This demo requires OpenGL 2.0 or higher.");
                    if (error == GLFW_NOT_INITIALIZED)
                        System.err.println();
                    if (error == GLFW_NO_CURRENT_CONTEXT)
                        System.err.println("GLFW_NO_CURRENT_CONTEXT");
                    if (error == GLFW_INVALID_ENUM)
                        System.err.println("GLFW_INVALID_ENUM");
                    if (error == GLFW_INVALID_VALUE)
                        System.err.println("GLFW_INVALID_VALUE");
                    if (error == GLFW_OUT_OF_MEMORY)
                        System.err.println("GLFW_OUT_OF_MEMORY");
                    if (error == GLFW_API_UNAVAILABLE)
                        System.err.println("GLFW_API_UNAVAILABLE");
                    if (error == GLFW_VERSION_UNAVAILABLE)
                        System.err.println("GLFW_VERSION_UNAVAILABLE");
                    if (error == GLFW_PLATFORM_ERROR)
                        System.err.println("GLFW_PLATFORM_ERROR");
                    if (error == GLFW_FORMAT_UNAVAILABLE)
                        System.err.println("GLFW_FORMAT_UNAVAILABLE");
                    if (error == GLFW_FORMAT_UNAVAILABLE)
                        System.err.println("GLFW_FORMAT_UNAVAILABLE");

                    delegate.invoke(error, description);
                }

                @Override
                public void free() {
                    delegate.free();
                }
            });
        //TO-DO
		/*other debug modes, some may have a very negative impact on performance
		 * see https://github.com/LWJGL/lwjgl3-wiki/wiki/2.5.-Troubleshooting
		 
		Configuration.DEBUG.set(true);
		Configuration.DEBUG_LOADER.set(true);
		Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
		Configuration.DEBUG_STACK.set(true);
		Configuration.DEBUG_STREAM.set(true);
		*/

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (videoMode.width() - pWidth.get(0)) / 2,
                    (videoMode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        if (DEBUG) {
            GLUtil.setupDebugMessageCallback();
        }

        renderer.init();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {

            renderer.display();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

}
