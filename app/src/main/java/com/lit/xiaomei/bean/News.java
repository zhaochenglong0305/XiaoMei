package com.lit.xiaomei.bean;

import java.io.Serializable;
import java.util.List;

public class News implements Serializable {


	private String Code;
	private List<ListDataBean> ListData;

	public String getCode() {
		return Code;
	}

	public void setCode(String Code) {
		this.Code = Code;
	}

	public List<ListDataBean> getListData() {
		return ListData;
	}

	public void setListData(List<ListDataBean> ListData) {
		this.ListData = ListData;
	}

	public static class ListDataBean {
		/**
		 * ID : 1
		 * ClassID : 1
		 * Title : 测试广告
		 * ContentTxt : 欢迎使用本物流管理软件。。。。。。。。
		 * ImageName :
		 */

		private String ID;
		private String ClassID;
		private String Title;
		private String ContentTxt;
		private String ImageName;

		public String getID() {
			return ID;
		}

		public void setID(String ID) {
			this.ID = ID;
		}

		public String getClassID() {
			return ClassID;
		}

		public void setClassID(String ClassID) {
			this.ClassID = ClassID;
		}

		public String getTitle() {
			return Title;
		}

		public void setTitle(String Title) {
			this.Title = Title;
		}

		public String getContentTxt() {
			return ContentTxt;
		}

		public void setContentTxt(String ContentTxt) {
			this.ContentTxt = ContentTxt;
		}

		public String getImageName() {
			return ImageName;
		}

		public void setImageName(String ImageName) {
			this.ImageName = ImageName;
		}
	}
}
