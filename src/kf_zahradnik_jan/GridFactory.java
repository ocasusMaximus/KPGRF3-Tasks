package kf_zahradnik_jan;


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

            for (int j = 0; j < m - 1; j++) {


                ib[index2++] = j + rowOffset;
                ib[index2++] = j + m + rowOffset;
                ib[index2++] = j + 1 + rowOffset;

                ib[index2++] = j + 1 + rowOffset;
                ib[index2++] = j + m + rowOffset;
                ib[index2++] = j + m + 1 + rowOffset;
            }
        }

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

        int[] ib = new int[m * n + (n - 1) + (m - 1) * (n - 1) - 1];
        int index2 = 0;
        for (int i = 0; i < n - 1; i++) {
            int rowOffset = i * m;
            if (i % 2 == 0) {
                for (int j = 0; j < m; j++) {
                    ib[index2++] = rowOffset + j;
                    ib[index2++] = rowOffset + j + m;
                    if (j == m - 1) ib[index2++] = rowOffset + j + m;
                }

            } else {
                for (int col = m - 1; col >= 0; col--) {
                    ib[index2++] = rowOffset + col;
                    ib[index2++] = rowOffset + col + m;
                    if (col == 0) ib[index2++] = rowOffset + col + m;
                }

            }


        }


        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 2)
        };
        return new OGLBuffers(vb, attributes, ib);
    }



}
