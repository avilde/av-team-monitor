package server.team.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "tm_setting", schema = "team_monitor", catalog = "")
public class TmSetting {
    private int rowId;
    private String name;
    private String value;
    private String type;

    @Id
    @Column(name = "row_id", nullable = false)
    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "value", nullable = false, length = 200)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Basic
    @Column(name = "type", nullable = false, length = 10, updatable = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TmSetting that = (TmSetting) o;

        if (rowId != that.rowId) return false;
        if (name != that.name) return false;
        if (value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rowId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return rowId + " | " + name + " | " + value + " | " + type;
    }
}
