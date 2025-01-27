SELECT DISTINCT
	TOP 1 <coluna> + 1 AS rangestart
FROM
	dbo.<tabela> AS A
WHERE
	NOT EXISTS ( SELECT *
				 FROM dbo.<tabela> AS B
                 WHERE B.<coluna> = A.<coluna> + 1
                )
    AND <coluna> < (SELECT MAX(<coluna>) FROM dbo.<tabela>)
ORDER BY 1