package Bean;

import java.sql.Date;

public class ResultsBean {

	private Date resultsDate;
	private String omikujiId;
	private String birthday;
	private String changer;
	private String updateDate;
	private String author;
	private String createDate;
	private int receiveHalfMonthResultsData;
	private String todaysBirthday;
	private OmikujiBean ob;
	private int receiveTodayResultsDataNum;

	public Date getResultsDate() {
		return resultsDate;
	}

	public void setResultsDate(Date resultsDate) {
		this.resultsDate = resultsDate;
	}

	public String getOmikujiId() {
		return omikujiId;
	}

	public void setOmikujiId(String omikujiId) {
		this.omikujiId = omikujiId;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getChanger() {
		return changer;
	}

	public void setChanger(String changer) {
		this.changer = changer;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String update_date) {
		this.updateDate = update_date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getReceiveHalfMonthResultsData() {
		return receiveHalfMonthResultsData;
	}

	public void setReceiveHalfMonthResultsData(int receiveHalfMonthResultsData) {
		this.receiveHalfMonthResultsData = receiveHalfMonthResultsData;
	}

	public String getTodaysBirthday() {
		return todaysBirthday;
	}

	public void setTodaysBirthday(String todaysBirthday) {
		this.todaysBirthday = todaysBirthday;
	}

	public OmikujiBean getOb() {
		return ob;
	}

	public void setOb(OmikujiBean ob) {
		this.ob = ob;
	}

	public int getReceiveTodayResultsDataNum() {
		return receiveTodayResultsDataNum;
	}

	public void setReceiveTodayResultsDataNum(int receiveTodayResultsDataNum) {
		this.receiveTodayResultsDataNum = receiveTodayResultsDataNum;
	}

	/**
	 * ?
	 */
	@Override
	public String toString() {
		return "ResultsBean [results_date=" + resultsDate + ", omikuji_id=" + omikujiId + ", birthday=" + birthday
				+ ", changer=" + changer + ", update_date=" + updateDate + ", author=" + author + ", create_date="
				+ createDate + "]";
	}

}
