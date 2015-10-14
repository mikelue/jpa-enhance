package guru.mikelue.jpa.data;

import guru.mikelue.jpa.data.DbValueGetter;

public enum FakeStatus implements DbValueGetter<Integer> {
	Wait(1), Working(2), Stop(4);

	private int dbValue;
	FakeStatus(int newDbValue)
	{
		dbValue = newDbValue;
	}

	@Override
	public Integer getDbValue()
	{
		return dbValue;
	}
}
