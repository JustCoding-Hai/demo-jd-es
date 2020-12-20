package top.javahai.demojdes.bean;

/**
 * @author Hai
 * @program: demo-jp-es
 * @description: 商品内容
 * @create 2020/12/20 - 21:06
 **/
public class Content {
    private String img;
    private String title;
    private String price;

    public Content(String img, String title, String price) {
        this.img = img;
        this.title = title;
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Content{" +
                "img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
