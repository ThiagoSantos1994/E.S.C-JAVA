INSERT INTO tbd_ConfiguracoesLancamentos
    (
    dt_ViradaMes,
    ds_MesReferencia,
    id_Funcionario,
    tp_ViradaAutomatica
    )
VALUES (
    0,
    month(getdate()),
    :idFuncionario,
    'N'
)
