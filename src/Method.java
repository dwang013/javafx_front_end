import javafx.scene.control.CheckBox;
public class Method {

    private CheckBox Method;
    private String Value[];
	private CheckBox CBox[];

    public Method(CheckBox method, String value[], CheckBox cb[])
	{
        Method = method;
        Value = value;
		CBox = cb;
    }

    public CheckBox getMethod()
	{
        return Method;
    }

    public void setMethod(CheckBox method)
	{
        Method = method;
    }

    public String getValue(int j) 
	{
        return Value[j];
    }

    public void setValue(int j, String value) 
	{
        Value[j] = value;
    }
	
	public CheckBox getCBox(int j)
	{
		return CBox[j];
	}
	
	public void setCBox(int j, CheckBox cb)
	{
		CBox[j] = cb;
	}
	
	public CheckBox[] getAllCBox()
	{
		return CBox;
	}
}