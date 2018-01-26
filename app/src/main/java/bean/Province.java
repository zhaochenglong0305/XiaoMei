package bean;

import utils.DataBaseUtils.Table;

/**
 * Created by Adminis on 2018/1/26.
 */
@Table(name = "T_Province")
public class Province {
    /**
     * 省
     */
    @Table.Column(name = "ProName", type = Table.Column.TYPE_STRING, isPrimaryKey = true, isNull = false)
    private String ProName;
    /**
     * 省编号
     */
    @Table.Column(name = "ProSort", type = Table.Column.TYPE_STRING)
    private String ProSort;
    /**
     * 类型
     */
    @Table.Column(name = "ProRemark", type = Table.Column.TYPE_STRING)
    private String ProRemark;

    public String getProName() {
        return ProName;
    }

    public void setProName(String proName) {
        ProName = proName;
    }

    public String getProSort() {
        return ProSort;
    }

    public void setProSort(String proSort) {
        ProSort = proSort;
    }

    public String getProRemark() {
        return ProRemark;
    }

    public void setProRemark(String proRemark) {
        ProRemark = proRemark;
    }
}
