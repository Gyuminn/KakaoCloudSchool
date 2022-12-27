public class ToDoVo {
    private Integer userid;
    private Integer id;
    private String title;
    private Boolean completed;

    public ToDoVo() {
        super();
    }

    public ToDoVo(Integer userid, Integer id, String title, Boolean completed) {
        super();
        this.userid = userid;
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "ToDoVo{" +
                "userid=" + userid +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }
}
