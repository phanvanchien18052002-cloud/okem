package beatdz;

import com.tgame.model.Caption;

public class LangLa_DoiIP extends LangLa_fo {
    private mTextBox tbIP;
    private mTextBox tbPort;
    private mTextBox tbWebData;
    
    public static final String DEFAULT_IP = "14.225.204.32";
    public static final int DEFAULT_PORT = 19129;
    public static final String DEFAULT_WEBDATA = "http://14.225.231.69/~laydatac/";

    public LangLa_DoiIP(MainScreen screen) {
        this.p = screen;
        this.a(240, 165);

        String savedIP = null;
        String savedPort = null;
        String savedWeb = null;

        try {
            savedIP = Binary.readUTF("custom_ip");
        } catch (Exception ignored) {}
        try {
            savedPort = Binary.readUTF("custom_port");
        } catch (Exception ignored) {}
        try {
            savedWeb = Binary.readUTF("custom_webdata");
        } catch (Exception ignored) {}

        if (savedIP == null || savedIP.trim().length() == 0) {
            savedIP = (DataCenter.gI().ipServer != null && DataCenter.gI().ipServer.length() > 0) 
                      ? DataCenter.gI().ipServer : DEFAULT_IP;
        }

        if (savedPort == null || savedPort.trim().length() == 0) {
            savedPort = String.valueOf(DataCenter.gI().portServer > 0 ? DataCenter.gI().portServer : DEFAULT_PORT);
        }

        if (savedWeb == null || savedWeb.trim().length() == 0) {
            savedWeb = (DataCenter.gI().aU != null && DataCenter.gI().aU.length() > 0) 
                       ? DataCenter.gI().aU : DEFAULT_WEBDATA;
        }

        // Tạo 3 ô nhập liệu mTextBox
        this.tbIP = this.a(75, 33, 145, "", this, 0);
        this.tbIP.a = 50;
        this.tbIP.a(savedIP.trim());

        this.tbPort = this.a(75, 60, 145, "", this, 0);
        this.tbPort.a = 6;
        this.tbPort.a(savedPort.trim());

        this.tbWebData = this.a(75, 87, 145, "", this, 0);
        this.tbWebData.a = 120;
        this.tbWebData.a(savedWeb.trim());

        // 2 nút ở cạnh đáy: Trái (Lưu), Phải (Mặc định) dùng style 7 để kích hoạt a(LangLa_jz)
        this.a(this.width / 2 - 65, this.height - 29, "Lưu", this, 1001, 7);
        this.a(this.width / 2 + 5, this.height - 29, "Mặc định", this, 1002, 7);
    }

    public void a(Graphics g) {
        this.a(g, this.cx, this.cy, this.width, this.height, "Cấu Hình IP Server", (byte)2, true);
        mFont.a(mFont.d, g, "IP Server:", 10, 33 + 5, 0, -1);
        mFont.a(mFont.d, g, "Port:", 10, 60 + 5, 0, -1);
        mFont.a(mFont.d, g, "Web Data:", 10, 87 + 5, 0, -1);
    }

    public void a(LangLa_jz jz, int x, int y) {
        switch (jz.b) {
            case 1001: // Bấm Lưu
                saveAndApply();
                this.J();
                break;
            case 1002: // Bấm Mặc định
                resetDefault();
                this.J();
                break;
            default:
                break;
        }
    }

    public void a(int var1, Object var2) {
    }

    public void a(int var1, Object var2, LangLa_cy var3) {
    }

    public void b() {
        if (this.tbIP != null) this.tbIP.b();
        if (this.tbPort != null) this.tbPort.b();
        if (this.tbWebData != null) this.tbWebData.b();
    }

    private void saveAndApply() {
        try {
            String ip = this.tbIP.f() != null ? this.tbIP.f().trim() : "";
            String portStr = this.tbPort.f() != null ? this.tbPort.f().trim() : "";
            String web = this.tbWebData.f() != null ? this.tbWebData.f().trim() : "";

            if (ip.length() == 0) {
                ip = DEFAULT_IP;
            }

            int port = DEFAULT_PORT;
            try {
                if (portStr.length() > 0) {
                    port = Integer.parseInt(portStr);
                }
            } catch (Exception ex) {
                port = DEFAULT_PORT;
            }

            // Lưu vào bộ nhớ máy để các lần sau tự tải lại
            Binary.writeUTF("custom_ip", ip);
            Binary.writeUTF("custom_port", String.valueOf(port));

            if (web.length() > 0) {
                if (!web.endsWith("/")) {
                    web = web + "/";
                }
                Binary.writeUTF("custom_webdata", web);
                DataCenter.gI().aU = web;
                DataCenter.gI().aV = web + "checkpc.txt";
                DataCenter.gI().aW = web + "checkandroid.txt";
                DataCenter.gI().aX = web + "checkios.txt";
            }

            applyServer(ip, port);

            if (Session.gI().isConnected()) {
                Session.gI().close();
            }

            if (DataCenter.gI().currentScreen != null) {
                DataCenter.gI().currentScreen.showMessage("Đã đổi sang: " + ip + ":" + port, -2560);
            }
        } catch (Exception e) {
            Utlis.println(e);
        }
    }

    private void resetDefault() {
        try {
            Binary.writeUTF("custom_ip", "");
            Binary.writeUTF("custom_port", "");
            Binary.writeUTF("custom_webdata", "");

            DataCenter.gI().aU = DEFAULT_WEBDATA;
            DataCenter.gI().aV = DEFAULT_WEBDATA + "checkpc.txt";
            DataCenter.gI().aW = DEFAULT_WEBDATA + "checkandroid.txt";
            DataCenter.gI().aX = DEFAULT_WEBDATA + "checkios.txt";

            applyServer(DEFAULT_IP, DEFAULT_PORT);

            if (Session.gI().isConnected()) {
                Session.gI().close();
            }

            if (DataCenter.gI().currentScreen != null) {
                DataCenter.gI().currentScreen.showMessage("Đã khôi phục IP mặc định!", -2560);
            }
        } catch (Exception e) {
            Utlis.println(e);
        }
    }

    public static void applyServer(String ip, int port) {
        try {
            DataCenter.gI().ipServer = ip;
            DataCenter.gI().portServer = port;

            Server sv = new Server();
            sv.id = (short) 999;
            sv.name = "Máy Chủ (" + ip + ")";
            sv.ip = ip;
            sv.port = (short) port;

            DataCenter.gI().aP = sv;
            DataCenter.gI().selectServer = sv;
            Binary.writeUTF("arr_server", ip + ":" + port);
        } catch (Exception e) {
            Utlis.println(e);
        }
    }

    public static void checkAutoLoadCustomServer() {
        try {
            String savedIP = Binary.readUTF("custom_ip");
            String savedPort = Binary.readUTF("custom_port");
            String savedWeb = Binary.readUTF("custom_webdata");

            if (savedWeb != null && savedWeb.trim().length() > 0) {
                String web = savedWeb.trim();
                if (!web.endsWith("/")) web += "/";
                DataCenter.gI().aU = web;
                DataCenter.gI().aV = web + "checkpc.txt";
                DataCenter.gI().aW = web + "checkandroid.txt";
                DataCenter.gI().aX = web + "checkios.txt";
            }

            if (savedIP != null && savedIP.trim().length() > 0) {
                int port = DEFAULT_PORT;
                try {
                    if (savedPort != null && savedPort.trim().length() > 0) {
                        port = Integer.parseInt(savedPort.trim());
                    }
                } catch (Exception ignored) {}

                applyServer(savedIP.trim(), port);
            }
        } catch (Exception e) {
            Utlis.println(e);
        }
    }
}
