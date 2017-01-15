package com.webmagic;

import java.util.Map;

import com.services.HouseUtil;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class DBPipeline implements Pipeline {
	@Override
	public void process(ResultItems resultItems, Task task) {
		String url = resultItems.getRequest().getUrl();
		if (url != null && url.contains("version")) {
			Map<String, Object> result = resultItems.getAll();
			HouseUtil.saveHouse(result, url);
		}
	}

}
