/*
 * Copyright 2015 Skymind,Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.deeplearning4j.plot;

import org.apache.commons.io.EndianUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by agibsonccc on 10/1/14.
 */
public class BarnesHutTsneTest {

    @Test
    public void testTsne() throws Exception {
        Nd4j.ENFORCE_NUMERICAL_STABILITY = true;
        Nd4j.MAX_ELEMENTS_PER_SLICE = Integer.MAX_VALUE;
        Nd4j.MAX_SLICES_TO_PRINT = Integer.MAX_VALUE;
        Nd4j.getRandom().setSeed(123);
        BarnesHutTsne b = new BarnesHutTsne.Builder().stopLyingIteration(250)
                .theta(0.5).learningRate(200).useAdaGrad(true)
                .build();

        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(new File("/home/agibsonccc/code/barneshut/data.dat"),false));
        DataOutputStream dos = new DataOutputStream(fos);
        ClassPathResource resource = new ClassPathResource("/mnist2500_X.txt");
        File f = resource.getFile();
        INDArray data = Nd4j.readTxt(f.getAbsolutePath(),"   ");


        EndianUtils.writeSwappedInteger(dos,data.rows());
        EndianUtils.writeSwappedInteger(dos, data.columns());
        EndianUtils.writeSwappedDouble(dos, 0.5);
        EndianUtils.writeSwappedDouble(dos, 30);
        EndianUtils.writeSwappedInteger(dos, 2);
        for(int i = 0; i < data.rows(); i++) {
            for(int j = 0; j < data.columns(); j++)
                EndianUtils.writeSwappedDouble(dos,data.getDouble(i,j));
        }

        EndianUtils.writeSwappedInteger(dos,123);

        dos.flush();
        fos.flush();
        dos.close();

        ClassPathResource labels = new ClassPathResource("mnist2500_labels.txt");
        List<String> labelsList = IOUtils.readLines(labels.getInputStream());
        b.fit(data);
    }


}
