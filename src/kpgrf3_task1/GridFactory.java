package kpgrf3_task1;
//package lvl2advanced.p01gui.p01simple;

import lwjglutils.OGLBuffers;

class GridFactory {

    /**
     * @param m počet vrcholů v řádku
     * @param n počet vrcholů ve sloupci
     * @return OGLBuffers
     */
    static OGLBuffers generateGridTriangleList(int m, int n) {
        float[] vb = new float[m * n * 2];
        int index = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                vb[index++] = j / (float) (m - 1);
                vb[index++] = i / (float) (n - 1);
            }
        }

        int[] ib = new int[2 * 3 * (m - 1) * (n - 1)];
        int index2 = 0;

        for (int i = 0; i < n - 1; i++) {
            int rowOffset = i * m;
//            System.out.println("Radek");
            for (int j = 0; j < m - 1; j++) {
//                System.out.println(j + rowOffset);
//                System.out.println(j + m + rowOffset);
//                System.out.println(j + 1 + rowOffset);
//
//                System.out.println(j + 1 + rowOffset);
//                System.out.println(j + m + rowOffset);
//                System.out.println(j + m + 1 + rowOffset);
//
//                System.out.println("----------------------------------");

                ib[index2++] = j + rowOffset;
                ib[index2++] = j + m + rowOffset;
                ib[index2++] = j + 1 + rowOffset;

                ib[index2++] = j + 1 + rowOffset;
                ib[index2++] = j + m + rowOffset;
                ib[index2++] = j + m + 1 + rowOffset;
            }
        }
//        System.out.println("***************************");
//        return null;
        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 2)
        };
        return new OGLBuffers(vb, attributes, ib);
    }

    /**
     * @param m počet vrcholů v řádku
     * @param n počet vrcholů ve sloupci
     * @return OGLBuffers
     */
    static OGLBuffers generateGridTriangleStrips(int m, int n) {
        //stejny jak pro list tak pro strip
        float[] vb = new float[m * n * 2];
        int index = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                vb[index++] = j / (float) (m - 1);
                vb[index++] = i / (float) (n - 1);
            }
        }

        int[] ib = new int[2 * m * (n - 1)];
        int index2 = 0;

        for (int i = 0; i < n - 1; i++) {
            int rowOffset = i * m;

            System.out.println("Radek");
            for (int j = 0; j < m -1; j++) {
                System.out.println(j + rowOffset);
                System.out.println(j + m + rowOffset);
                System.out.println(j + 1 + rowOffset);

                System.out.println(j + m + rowOffset);
                System.out.println(j + 1 + rowOffset);
                System.out.println(j + m + 1 + rowOffset);

                System.out.println("----------------------------------");
//
//                ib[index2++] = j + rowOffset;
//                ib[index2++] = j + m + rowOffset;
//                ib[index2++] = j + 1 + rowOffset;
//
//                ib[index2++] = j + m + rowOffset;
//                ib[index2++] = j + 1 + rowOffset;
//                ib[index2++] = j + m + 1 + rowOffset;

            }
//


            System.out.println("***************************");

        }
        return null;
//
//        OGLBuffers.Attrib[] attributes = {
//                new OGLBuffers.Attrib("inPosition", 2)
//        };
//        return new OGLBuffers(vb, attributes, ib);
    }

    public static void main(String[] args) {
//        generateGridTriangleList(4, 4);
        generateGridTriangleStrips(4, 4);
    }

}
