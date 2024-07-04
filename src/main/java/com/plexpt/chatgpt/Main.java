package com.plexpt.chatgpt;


public class Main {
    public static void main(String[] args) {
        //国内需要代理
//        Proxy proxy = Proxys.socks5("127.0.0.1", 10808);
        //socks5 代理
        // Proxy proxy = Proxys.socks5("127.0.0.1", 1080);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("sk-pBruIqC3hPClr0qP1c220a2d4294430984A1Bc5429Cc5cE3")
                .apiHost("https://api.xiaoai.plus") //反向代理地址
                .build()
                .init();

        String res = chatGPT.chat("写一段七言绝句诗，题目是：火锅！");
        System.out.println(res);
        //国内需要代理

    }
}
