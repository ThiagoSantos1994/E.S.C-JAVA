INSERT INTO tbd_Consolidacao
    (
    ds_TituloConsolidacao,
    id_Funcionario,
    dt_Cadastro,
    tp_Baixado
    )
VALUES (
    :consolidacao.dsTituloConsolidacao,
    :consolidacao.idFuncionario,
    CONVERT (date, GETDATE()),
    'N'
)
