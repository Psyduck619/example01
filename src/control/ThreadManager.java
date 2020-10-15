package control;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static control.FileManager.readFileContent;

public class ThreadManager {

    //ThreadA
    public static void getEN(String str) throws IOException {
        String s = "\\w+";
        Pattern pattern = Pattern.compile(s);
        Matcher ma = pattern.matcher(str);
        ArrayList<String> arr = new ArrayList<String>();
        while (ma.find()) {
            if((ma.group().length() != 1 || ma.group().equals("a") || ma.group().equals("i"))
                    && !arr.contains(ma.group())) {
                arr.add(ma.group());
            }
        }
        Collections.sort(arr);
        BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\txt\\threadA.txt"));
        for (String word:arr) {
            bw.write(word);
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }
//    //ThreadB
//    public static void getCH(String str) throws IOException {
//        Map<String, Integer> map = new HashMap<String, Integer>();
//        String reg = "[^\\u4e00-\\u9fa5]";
//        str = str.replaceAll("reg", "");//去除英文和数字
//        //遍历
//        for (int i = 0; i < str.length(); i++) {
//            boolean flag = false;
//            for (Map.Entry<String, Integer> entry : map.entrySet()) {
//                String mapKey = entry.getKey();
//                Integer value = entry.getValue();
//                if (str.substring(i, i + 1).equals(mapKey)) {
//                    flag = true;
//                    map.put(mapKey, ++value);
//                    break;
//                }
//            }
//            if (!flag) {
//                map.put(str.substring(i, i + 1), 1);
//            }
//        }
//
//        //根据使用频次排序
//        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
//        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
//            @Override
//            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//                return o2.getValue().compareTo(o1.getValue());
//            }
//        });
//        //输出为文本文件
//        BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\真文件夹\\20-21-1\\安卓\\实验一\\threadB.txt"));
//        for (Map.Entry<String, Integer> entry : list) {
//            bw.write(entry.getKey() + "    time:" + entry.getValue());
//            bw.newLine();
//            bw.flush();
//        }
//        bw.close();
//    }

    //ThreadB
    public static void getCH(String str) throws IOException {
        Map<String, Integer> map = new HashMap<String, Integer>();
        List<Word> lists = getWords(str);
        //遍历中文单词
        for (int i = 0; i < lists.size(); i++) {
            boolean flag = false;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String mapKey = entry.getKey();
                Integer value = entry.getValue();
                if (lists.get(i).toString().equals(mapKey)) {
                    flag = true;
                    map.put(mapKey, ++value);
                    break;
                }
            }
            if (!flag) {
                map.put(lists.get(i).toString().replaceAll("[^\\u4E00-\\u9FA5]", ""), 1);
            }
        }

        //根据使用频次排序
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        //输出为文本文件
        BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\txt\\threadB.txt"));
        for (Map.Entry<String, Integer> entry : list) {
            bw.write(entry.getKey() + "    time:" + entry.getValue());
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }

    //ThreadC
    public static void sum() throws IOException {
        ArrayList<String> res = new ArrayList<String>();//段落切分结果
        StringBuilder sb = new StringBuilder();//拼接读取的内容
        String temp = null;//临时变量
        BufferedReader reader = new BufferedReader(new FileReader(new File("D:\\txt\\Z01-Example.txt")));
        int ch = 0;
        while ((ch = reader.read()) != -1) {
            temp = sb.toString().trim().replaceAll("\\s+", " ");
            if ((char) ch == '\r') {
                // 判断是否是空行
                if (!"".equals(temp)) {
                    // 说明到了段落结尾，将其加入链表，并清空sb
                    res.add(temp);
                }
                sb.delete(0, sb.length());//清空sb
                temp = null;
            } else {
                // 说明没到段落结尾，将结果暂存
                sb.append((char) ch);
            }
        }
        //如果最后一段非空，将最后一段加入，否则不处理
        if (!"".equals(temp)) {
            res.add(temp);
        }
        //初始化统计量
        int ew = 0;
        int cw = 0;
        int tw = 0;
        int tew = 0;
        int tcw = 0;
        int ttw = 0;
        int count = 0;
        Iterator<String> iterable = res.iterator();//使用迭代器统计
        while (iterable.hasNext()) {
            String next = iterable.next();
            ew = cw = tw = 0;
            count++;
            //统计英文词数
            Pattern pattern = Pattern.compile("\\w+");
            Pattern pattern2 = Pattern.compile(".*\\d+.*");
            Matcher m1 = pattern.matcher(next);
            while (m1.find()) {
                Matcher m2 = pattern2.matcher(m1.group());
                if (!m2.matches()) {
                    ew++;
                }
            }
            //统计中文词数
            String reg = "[^\\u4e00-\\u9fa5]";
            String str = next.replaceAll(reg, "");
            cw = str.length();
            //总计
            tw = ew + cw;
            tew += ew;
            tcw += cw;
            ttw += tw;

            System.out.println("第" + count + "段：");
            System.out.println(next);
            System.out.println("中文字数：" + cw + "   英文单词数：" + ew + "   总计：" + tw + "\n");
        }
        System.out.println("段落总个数：" + res.size() + "   中文字总数：" + tcw + "   英文单词总数：" + tew + "   字数总计:" + ttw);

    }

    //获取单词集合
    public static List getWords(String str) {

        str = str.replaceAll("\\w+", "");

        //移除停用词进行分词
        List<Word> list = WordSegmenter.seg(str);

        //保留停用词
        List<Word> lists = WordSegmenter.segWithStopWords(str);

//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(lists.get(i));
//        }
        return list;

    }

//    public static void main(String[] args) {
//        String str = readFileContent("D:\\txt\\Z01-Example.txt");
//        try {
//            getCH(str);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
