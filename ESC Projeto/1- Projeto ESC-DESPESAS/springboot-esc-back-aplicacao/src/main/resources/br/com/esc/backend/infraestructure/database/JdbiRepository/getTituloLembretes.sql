SELECT
	id_Lembrete,
	ds_TituloLembrete,
	id_Funcionario
FROM
	tbd_CadastroLembretes
WHERE
    tp_Baixado = :tpBaixado
    AND id_Funcionario = :idFuncionario
ORDER BY
    ds_TituloLembrete ASC