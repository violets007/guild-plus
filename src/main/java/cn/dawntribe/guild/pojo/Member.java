package cn.dawntribe.guild.pojo;

/**
 * @author violets007
 * @version 1.0
 * @description: 公会成员
 * @date 2022/2/27 11:37 PM
 */
public class Member {
    private String name;
    private String post;
    private int jobLevel;
    private String joinTime;
    private Double contribute;

    public Member() {
    }

    public Member(String name, String post, int jobLevel, String joinTime, Double contribute) {
        this.name = name;
        this.post = post;
        this.jobLevel = jobLevel;
        this.joinTime = joinTime;
        this.contribute = contribute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(int jobLevel) {
        this.jobLevel = jobLevel;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public Double getContribute() {
        return contribute;
    }

    public void setContribute(Double contribute) {
        this.contribute = contribute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        if (!name.equals(member.name)) return false;
        return joinTime.equals(member.joinTime);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + joinTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", post='" + post + '\'' +
                ", jobLevel='" + jobLevel + '\'' +
                ", joinTime='" + joinTime + '\'' +
                ", contribute=" + contribute +
                '}';
    }
}
