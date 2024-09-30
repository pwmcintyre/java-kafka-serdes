package com.pwmcintyre.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Movie {
	public final String name;
	public final String producer;
}
