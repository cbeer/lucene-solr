/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.analysis.icu;

import java.io.IOException;
import java.io.Reader;

import com.ibm.icu.text.Transliterator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;

public class ICUTransformCharFilterTest extends BaseTokenStreamTestCase {

  public void testBasicFunctionality() throws Exception {
//    checkToken(Transliterator.getInstance("Traditional-Simplified"),
//        "簡化字", new String[] { "简化字" });
//    checkToken(Transliterator.getInstance("Katakana-Hiragana"),
//        "ヒラガナ", new String[] { "ひらがな" });
//    checkToken(Transliterator.getInstance("Fullwidth-Halfwidth"),
//        "アルアノリウ", new String[] { "ｱﾙｱﾉﾘｳ" });
    checkToken(Transliterator.getInstance("Any-Latin"),
        "Αλφαβητικός Κατάλογος", new String[] { "Alphabētikós", "Katálogos" });
//    checkToken(Transliterator.getInstance("NFD; [:Nonspacing Mark:] Remove"),
//        "Alphabētikós Katálogos", new String[] { "Alphabetikos", "Katalogos" });
//    checkToken(Transliterator.getInstance("Han-Latin"),
//        "中国", new String[] { "zhōng", "guó" });
  }

  private void checkToken(Transliterator transform, String input, String[] expected) throws IOException {
    final Analyzer analyzer = buildAnalyzer(transform);
    assertAnalyzesTo(analyzer, input, expected);
    analyzer.close();
  }

  public Analyzer buildAnalyzer(final Transliterator transliterator) {
    return new Analyzer() {
      @Override
      protected TokenStreamComponents createComponents(String fieldName) {
        return new TokenStreamComponents(new MockTokenizer());
      }

      @Override
      protected Reader initReader(String fieldName, Reader reader) {
        return new ICUTransformCharFilter(reader, transliterator);
      }
    };
  }
}