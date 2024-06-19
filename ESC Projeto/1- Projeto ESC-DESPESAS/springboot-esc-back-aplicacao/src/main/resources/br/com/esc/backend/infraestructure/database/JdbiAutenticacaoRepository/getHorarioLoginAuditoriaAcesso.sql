SELECT 
	TOP 1 
	SUBSTRING(ds_DataHoraLogin, 1,10) AS data_Login,
	SUBSTRING(ds_DataHoraLogin, 12,5) AS hora_Login,
	SUBSTRING(CONVERT(VARCHAR(23), GETDATE(), 121) , 12, 5) AS hora_Atual,
	DATEDIFF(MINUTE, SUBSTRING(ds_DataHoraLogin, 12,5), SUBSTRING(CONVERT(VARCHAR(23), GETDATE(), 121) , 12, 5)) as tempo_Logado
FROM 
	tbd_AuditoriaAcesso 
WHERE 
	id_Funcionario = :idFuncionario
ORDER BY 
	id_Acesso DESC 