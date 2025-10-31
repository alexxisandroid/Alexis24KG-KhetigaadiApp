package com.khetigaadi.shop.payu;
public enum AppEnvironment {
    DEV {
        @Override
        public String merchant_Key() {
            return "u3k3ltrI";
        }
        @Override
        public String merchant_ID() {
            return "7188733";
        }
        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }
        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "V9OzzwnyCQ";
        }

        @Override
        public boolean debug() {
            return true;
        }
    },
    PRODUCTION {
        @Override
        public String merchant_Key() {
            return "u3k3ltrI";
        }

        @Override
        public String merchant_ID() {
            return "7188733";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "V9OzzwnyCQ";
        }

        @Override
        public boolean debug() {
            return false;
        }
    };

    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();

    public abstract boolean debug();
}





//package com.khetigaadi.shop.payu;
//
//public class AppEnvironment {
//    public static final AppEnvironment DEV = new AppEnvironment();
//    public static final AppEnvironment PRODUCTION = new AppEnvironment();
//    public static final String SURL = "";
//    public static final String FURL = "";
//    private static final String MERCHANT_KEY = "";
//    private static final String SALT = "";
//    public AppEnvironment() {
//    }
//    public String surl() {
//      return SURL;
//    }
//    public String furl(){
//      return FURL;
//    }
//    public boolean debug() {
//        return true;
//    }
//    public String merchant_Key() {
//        return MERCHANT_KEY;
//    }
//    public String salt() {
//        return SALT;
//    }
//}
