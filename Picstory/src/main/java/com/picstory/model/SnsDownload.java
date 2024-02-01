package com.picstory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnsDownload {
	private int sns_download_no;
	private int sns_downloader;
	private int sns_download_photo_no;
	private String sns_downloaded_at;

}
