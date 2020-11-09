package cn.fengcrush.lifestyle.entity;

public class ExpressTraces {
    /**
     * "traces": [
     *       {
     *         "time": "2017-12-26 19:52:30",
     *         "desc": "[济南市] [济南龙鼎分部]的南清已收件 电话:13113450088"
     *       }
     */
    private String time;
    private String desc;

    public ExpressTraces(){}

    public ExpressTraces(String time,String desc) {
        this.time = time;
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
