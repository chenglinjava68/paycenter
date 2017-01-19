package com.qccr.paycenter.facade.constants;

/**
 * 当面付返回
 * Created by user on 2016/11/21.
 */
public enum FacePayResultStatusEnum {

 //支付状态	status	是	Integer	1成功，2失败，4等待，7未授权，8已过期【主要判断这个参数】
        SUCCESS(1,"成功"),
        FAIL(2,"失败"),
        WAIT(4,"等待"),
        UNAUTHORIZED(7,"未授权"),//
        EXPIRED(8,"已过期");//
        private int value ;
        private String msg;

        FacePayResultStatusEnum(int value, String msg){
            this.value = value;
            this.msg = msg;
        }

        /**
         *
         * @param value
         * @return
         */
        public static FacePayResultStatusEnum valueOf(int value) {
            for(FacePayResultStatusEnum rcode : FacePayResultStatusEnum.values()){
                if(rcode.value == value){
                    return rcode;
                }
            }
            return null;
        }

        /**
         *
         * @param msg
         * @return
         */
        public static FacePayResultStatusEnum get(String msg) {
            for(FacePayResultStatusEnum config : FacePayResultStatusEnum.values()){
                if(config.msg.equals(msg)){
                    return config;
                }
            }
            return null;
        }

        public  int getValue(){

            return value;

        }

        public  String getMsg(){

            return msg;
        }
        @Override
        public String toString(){

            return String.valueOf(this.value);

        }

        public int getInfo(){

            return this.value;
        }

}
